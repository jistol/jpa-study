package io.jistol.github.jpademo.entity.advancemapping.join;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class FlatBook extends FlatItem {
    private String author;
}
