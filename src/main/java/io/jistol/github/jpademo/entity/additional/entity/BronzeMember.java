package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
public class BronzeMember implements MemberBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Company company;
}
