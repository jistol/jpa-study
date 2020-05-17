package io.jistol.github.jpademo.entity.advancemapping.singletable;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
public class Album extends Item {
    private String artist;
}
