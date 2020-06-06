package io.jistol.github.jpademo.entity.relate.entity;

import io.jistol.github.jpademo.entity.relate.entity.Member;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
@Data
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
    
    private String name;
    
    private int amount;
    
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
