package io.jistol.github.jpademo.entity.proxy;

import lombok.Data;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    
    @OneToMany(mappedBy = "member",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<Order> orders = new ArrayList<>();
}
