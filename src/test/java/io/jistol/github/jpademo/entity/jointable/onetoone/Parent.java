package io.jistol.github.jpademo.entity.jointable.onetoone;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@NoArgsConstructor
public class Parent {
    @Id
    @GeneratedValue
    @Column(name = "PARENT_ID")
    private Long id;
    
    private String name;
    
    @OneToOne
    @JoinTable(
            name = "PARENT_CHILD",
            joinColumns = @JoinColumn(name = "PARENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CHILD_ID", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = "CHILD_ID")
    )
    private Child child;
}
