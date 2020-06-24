package io.jistol.github.jpademo.entity.translock.entity;

import io.jistol.github.jpademo.entity.HasId;
import io.jistol.github.jpademo.entity.HasParent;
import io.jistol.github.jpademo.entity.HasVersion;
import io.jistol.github.jpademo.entity.NameSupport;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EagerChild implements NameSupport, HasId, HasParent<EagerParent>, HasVersion {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Version
    private Long version;

    @ManyToOne
    EagerParent parent;
}
