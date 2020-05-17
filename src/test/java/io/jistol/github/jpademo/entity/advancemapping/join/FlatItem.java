package io.jistol.github.jpademo.entity.advancemapping.join;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FlatItem {
    @Id
    @GeneratedValue
    @Column(name = "FLAT_ITEM_ID")
    private Long id;
    
    private String name;
}
