package io.jistol.github.jpademo.entity.advanced.entity;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.NameSupport;
import io.jistol.github.jpademo.entity.HasId;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class FetchEagerParent implements NameSupport, HasId, HasChild<FetchEagerChild> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<FetchEagerChild> childList = new ArrayList<>();
}
