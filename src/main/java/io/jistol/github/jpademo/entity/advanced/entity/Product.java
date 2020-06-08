package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NamedEntityGraph(name = "Product.withItem", attributeNodes = @NamedAttributeNode("productItems"))
@NamedEntityGraph(name = "Product.withDetail", attributeNodes = @NamedAttributeNode("productDetail"))
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductItem> productItems = new ArrayList<>();
}
