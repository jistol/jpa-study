package io.jistol.github.jpademo.entity.advancemapping.tableperclass;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Data
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}
