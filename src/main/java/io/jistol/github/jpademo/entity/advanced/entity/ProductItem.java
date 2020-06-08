package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class ProductItem {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Product product;
}
