package io.jistol.github.jpademo.entity.advanced.entity;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.NameSupport;
import io.jistol.github.jpademo.entity.HasId;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class BatchEagerParent implements NameSupport, HasId, HasChild<BatchEagerChild> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<BatchEagerChild> childList = new ArrayList<>();
}
