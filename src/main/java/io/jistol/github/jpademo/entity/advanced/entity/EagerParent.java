package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class EagerParent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "eagerParent", fetch = FetchType.EAGER)
    private List<EagerChild> childList = new ArrayList<>();
}
