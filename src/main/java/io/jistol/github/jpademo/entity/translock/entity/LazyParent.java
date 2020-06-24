package io.jistol.github.jpademo.entity.translock.entity;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.HasId;
import io.jistol.github.jpademo.entity.NameSupport;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class LazyParent implements NameSupport, HasId, HasChild<LazyChild> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<LazyChild> childList = new ArrayList<>();
}
