package io.jistol.github.jpademo.entity.manytomany;

import io.jistol.github.jpademo.entity.manytomany.Member;
import io.jistol.github.jpademo.entity.manytomany.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class ManyToManyTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void 단방향_다대다_테스트() {
        final String memberId = "MEM_A";
        final String productId = "PRO_A";
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        save(em, memberId, productId);
        // 1차캐시에서 조회
        log.warn("find in transaction");
        findMember(em, memberId);
        et.commit();
    
        // 1차캐시에서 조회
        log.warn("find after transaction");
        findMember(em, memberId);
        
        em.clear();
        
        // DB에서 조회
        log.warn("find after clear");
        findMember(em, memberId);
    
        // DB에서 조회
        log.warn("find by new EntityManager");
        findMember(entityManagerFactory.createEntityManager(), memberId);
    }
    
    public void findMember(EntityManager em, String memberId) {
        Member mem = em.find(Member.class, memberId);
        log.warn("mem is {}", mem.getUsername());
        log.warn("mem.products is {}", mem.getProducts().size());
    }
    
    
    public void save(EntityManager em, String memberId, String productId) {
        log.warn("save Start");
    
        Product product = new Product();
        product.setId(productId);
        product.setName("NAME_A");
        em.persist(product);
    
        Member member = new Member();
        member.setId(memberId);
        member.setUsername("USER_A");
        member.getProducts().add(product);
        em.persist(member);
    
        log.warn("save persist complete");
    
    
        log.warn("save End");
    }
}
