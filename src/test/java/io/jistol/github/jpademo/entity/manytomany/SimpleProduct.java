package io.jistol.github.jpademo.entity.manytomany;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SimpleProduct {
    @Id
    @Column(name = "PRODUCT_ID")
    private String id;
    
    private String name;
    
    @OneToMany(mappedBy = "product")
    private List<SimpleMemberProduct> members = new ArrayList<>();
}
