package io.jistol.github.jpademo.entity.jointable.manytoone;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
public class Child {
    @Id
    @GeneratedValue
    @Column(name = "CHILD_ID")
    private Long id;
    
    private String name;
    
    @ManyToOne
    @JoinTable(
            name = "PARENT_CHILD",
            joinColumns = @JoinColumn(name = "CHILD_ID"),
            inverseJoinColumns = @JoinColumn(name = "PARENT_ID")
    )
    private Parent parent;
}
