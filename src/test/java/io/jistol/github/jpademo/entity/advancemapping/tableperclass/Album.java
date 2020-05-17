package io.jistol.github.jpademo.entity.advancemapping.tableperclass;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Album extends Item {
    private String artist;
}
