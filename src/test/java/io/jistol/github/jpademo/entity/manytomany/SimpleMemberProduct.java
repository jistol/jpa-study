package io.jistol.github.jpademo.entity.manytomany;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
public class SimpleMemberProduct {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private SimpleMember member;
    
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private SimpleProduct product;
    
    private int orderAmount;
}
