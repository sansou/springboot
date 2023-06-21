package com.example.springboot.controlers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

  @Autowired
  ProductRepository prodRepository;

  @PostMapping
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    var productModel = new ProductModel();
    BeanUtils.copyProperties(productRecordDto, productModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(prodRepository.save(productModel));
  }

  @GetMapping
  public ResponseEntity<List<ProductModel>> getAllProducts() {
    List<ProductModel> productsList = prodRepository.findAll();
    if (!productsList.isEmpty()) {
      for (ProductModel product : productsList) {
        UUID id = product.getIdProduct();
        product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(productsList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
    Optional<ProductModel> product = prodRepository.findById(id);
    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

    }
    product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
    return ResponseEntity.status(HttpStatus.OK).body(product.get());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid ProductRecordDto productRecordDto) {
    Optional<ProductModel> product = prodRepository.findById(id);
    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    var productModel = product.get();
    BeanUtils.copyProperties(productRecordDto, productModel);
    return ResponseEntity.status(HttpStatus.OK).body(prodRepository.save(productModel));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
    Optional<ProductModel> product = prodRepository.findById(id);
    if (product.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    prodRepository.delete(product.get());
    return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
  }

}