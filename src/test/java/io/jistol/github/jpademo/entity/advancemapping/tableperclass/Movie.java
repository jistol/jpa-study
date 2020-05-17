package io.jistol.github.jpademo.entity.advancemapping.tableperclass;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
public class Movie extends Item {
    private String director;
    private String actor;
}
