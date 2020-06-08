package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class ProductDetail {
    @Id
    @GeneratedValue
    private Long id;

    private String quality;

    private Integer count;

    @OneToOne
    private Product product;
}
