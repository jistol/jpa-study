package io.jistol.github.jpademo.entity.translock.entity;

import io.jistol.github.jpademo.entity.HasId;
import io.jistol.github.jpademo.entity.HasParent;
import io.jistol.github.jpademo.entity.NameSupport;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class LazyChild implements NameSupport, HasId, HasParent<LazyParent> {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    LazyParent parent;
}
