package io.jistol.github.jpademo.entity.manytomany;

import io.jistol.github.jpademo.entity.manytomany.idclass.TriMemberProductId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@IdClass(TriMemberProductId.class)
@NoArgsConstructor
public class TriMemberProduct {
    @Id
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private TriMember member;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private TriProduct product;
    
    private int orderAmount;
}
