package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@DiscriminatorValue("M")
public class Movie extends Item {
    private String actor;
}
