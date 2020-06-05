package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class OrderInfo {
    public OrderInfo(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private GoldMember goldMember;

    @OneToMany(mappedBy = "orderInfo", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();
}
