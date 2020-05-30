package io.jistol.github.jpademo.entity.additional.entity;

import io.jistol.github.jpademo.entity.additional.converter.BooleanToYNConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"golds","silvers","bronzes"})
public class Company {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isCommerce;

    @OneToMany(mappedBy = "company")
    private Collection<GoldMember> golds = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<SilverMember> silvers = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private Set<BronzeMember> bronzes = new HashSet<>();
}
