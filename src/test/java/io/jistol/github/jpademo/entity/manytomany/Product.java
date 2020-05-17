package io.jistol.github.jpademo.entity.manytomany;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @Column(name = "PRODUCT_ID")
    private String id;
    
    private String name;
    
    /*@ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT_MAP",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    private List<Member> members;*/
}
