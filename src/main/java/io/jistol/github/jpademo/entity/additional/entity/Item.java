package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Item {
    public Item(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderInfo orderInfo;
}
