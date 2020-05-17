package io.jistol.github.jpademo.entity.advancemapping.join;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("A")
public class Album extends Item {
    private String artist;
}
