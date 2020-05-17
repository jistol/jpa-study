package io.jistol.github.jpademo.entity.manytomany;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class BiProduct {
    @Id
    @Column(name = "PRODUCT_ID")
    private String id;
    
    private String name;
    
    @ManyToMany(mappedBy = "products")
    private List<BiMember> members = new ArrayList<>();
}
