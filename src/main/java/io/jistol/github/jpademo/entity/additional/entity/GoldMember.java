package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class GoldMember implements MemberBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Company company;
}
