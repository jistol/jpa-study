package io.jistol.github.jpademo.entity.translock.entity;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.HasId;
import io.jistol.github.jpademo.entity.HasVersion;
import io.jistol.github.jpademo.entity.NameSupport;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class EagerParent implements NameSupport, HasId, HasChild<EagerChild>, HasVersion {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Version
    private Long version;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<EagerChild> childList = new ArrayList<>();
}
