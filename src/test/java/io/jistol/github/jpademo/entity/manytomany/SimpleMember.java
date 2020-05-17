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
public class SimpleMember {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    
    private String username;
    
    @OneToMany(mappedBy = "member")
    private List<SimpleMemberProduct> memberProducts = new ArrayList<>();
}
