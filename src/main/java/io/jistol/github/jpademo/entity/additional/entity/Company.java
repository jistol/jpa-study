package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "company")
    private Collection<GoldMember> golds = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<SilverMember> silvers = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private Set<BronzeMember> bronzes = new HashSet<>();
}
