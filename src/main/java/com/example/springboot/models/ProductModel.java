package com.example.springboot.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
  private static final long serialVersionUID = 1l;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID idProduct;
  private String name;
  private BigDecimal value;

}