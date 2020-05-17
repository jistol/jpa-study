package io.jistol.github.jpademo.entity.manytomany;

import io.jistol.github.jpademo.entity.manytomany.BiMember;
import io.jistol.github.jpademo.entity.manytomany.BiProduct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class BiManyToManyTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void 양방향_다대다_테스트() {
        final String memberId = "MEM_A";
        final String productId = "PRO_A";
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        save(em, memberId, productId);
        
        // 1차캐시에서 조회
        log.warn("find in transaction");
        findBiMember(em, memberId);
        et.commit();
    
        // 1차캐시에서 조회
        log.warn("find after transaction");
        findBiMember(em, memberId);
        
        em.clear();
        
        // DB에서 조회
        log.warn("find after clear");
        findBiMember(em, memberId);
    
        // DB에서 조회
        log.warn("find by new EntityManager");
        findBiMember(entityManagerFactory.createEntityManager(), memberId);
    }
    
    public void findBiMember(EntityManager em, String memberId) {
        BiMember mem = em.find(BiMember.class, memberId);
        log.warn("mem is {}", mem.getUsername());
        log.warn("mem.products is {}", mem.getProducts().size());
    }
    
    
    public void save(EntityManager em, String memberId, String productId) {
        log.warn("save Start");
    
        BiProduct product = new BiProduct();
        product.setId(productId);
        product.setName("NAME_A");
        em.persist(product);
    
        BiMember member = new BiMember();
        member.setId(memberId);
        member.setUsername("USER_A");
        em.persist(member);
    
        member.getProducts().add(product);
        product.getMembers().add(member);
        
        log.warn("save persist complete");
    
    
        log.warn("save End");
    }
}
