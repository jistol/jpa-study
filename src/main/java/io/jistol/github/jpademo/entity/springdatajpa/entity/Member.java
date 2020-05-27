package io.jistol.github.jpademo.entity.springdatajpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@NamedQueries(
        @NamedQuery(
                name = "Member.findByInfo",
                query = "SELECT m FROM Member m WHERE m.name = :name AND m.age = :age"
        )
)
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    private int age;
    
    @Embedded
    private Address address;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")
    private Team team;
}
