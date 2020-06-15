package io.jistol.github.jpademo.entity.advanced.entity;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("M")
public class Movie extends Item {
    private String actor;
}
