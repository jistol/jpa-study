package io.jistol.github.jpademo.entity.manytomany;

import io.jistol.github.jpademo.entity.manytomany.TriMember;
import io.jistol.github.jpademo.entity.manytomany.TriMemberProduct;
import io.jistol.github.jpademo.entity.manytomany.TriProduct;
import io.jistol.github.jpademo.entity.manytomany.idclass.TriMemberProductId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class TriManyToManyTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void 기본키사용_양방향_다대다_테스트() {
        final String memberId = "MEM_A";
        final String productId = "PRO_A";
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        save(em, memberId, productId);
        
        // 1차캐시에서 조회
        log.warn("find in transaction");
        findTriMember(em, memberId, productId);
        et.commit();
    
        // 1차캐시에서 조회
        log.warn("find after transaction");
        findTriMember(em, memberId, productId);
        
        em.clear();
        
        // DB에서 조회
        log.warn("find after clear");
        findTriMember(em, memberId, productId);
    
        // DB에서 조회
        log.warn("find by new EntityManager");
        findTriMember(entityManagerFactory.createEntityManager(), memberId, productId);
    }
    
    public void findTriMember(EntityManager em, String memberId, String productId) {
        TriMemberProductId idClass = new TriMemberProductId();
        idClass.setMember(memberId);
        idClass.setProduct(productId);
        TriMemberProduct memberProduct = em.find(TriMemberProduct.class, idClass);
        log.warn("member : {}", memberProduct.getMember().getId());
        log.warn("product : {}", memberProduct.getProduct().getId());
        log.warn("orderAmount : {}", memberProduct.getOrderAmount());
    }
    
    
    public void save(EntityManager em, String memberId, String productId) {
        log.warn("save Start");
    
        TriProduct product = new TriProduct();
        product.setId(productId);
        product.setName("NAME_A");
        em.persist(product);
    
        TriMember member = new TriMember();
        member.setId(memberId);
        member.setUsername("USER_A");
        em.persist(member);
    
        TriMemberProduct memberProduct = new TriMemberProduct();
        memberProduct.setMember(member);
        memberProduct.setProduct(product);
        memberProduct.setOrderAmount(100);
        em.persist(memberProduct);
    
        log.warn("save persist complete");
    
        log.warn("save End");
    }
}
