package io.jistol.github.jpademo.entity.manytomany;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class BiMember {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    
    private String username;
    
    @ManyToMany
    @JoinTable(name = "BI_MEMBER_PRODUCT_MAP",
        joinColumns = @JoinColumn(name = "MEMBER_ID"),
        inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private List<BiProduct> products = new ArrayList<>();
}
