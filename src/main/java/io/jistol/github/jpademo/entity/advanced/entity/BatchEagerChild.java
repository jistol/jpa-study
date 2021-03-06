package io.jistol.github.jpademo.entity.advanced.entity;

import io.jistol.github.jpademo.entity.HasParent;
import io.jistol.github.jpademo.entity.NameSupport;
import io.jistol.github.jpademo.entity.HasId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class BatchEagerChild implements NameSupport, HasId, HasParent<BatchEagerParent> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    BatchEagerParent parent;
}
