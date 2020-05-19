package io.jistol.github.jpademo.entity.oopquery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    private int age;
    
    @Embedded
    private Address address;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")
    private Team team;
}
