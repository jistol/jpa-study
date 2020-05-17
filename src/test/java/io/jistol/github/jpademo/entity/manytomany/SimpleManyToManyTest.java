package io.jistol.github.jpademo.entity.manytomany;

import io.jistol.github.jpademo.entity.manytomany.SimpleMember;
import io.jistol.github.jpademo.entity.manytomany.SimpleMemberProduct;
import io.jistol.github.jpademo.entity.manytomany.SimpleProduct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class SimpleManyToManyTest {
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
        findSimpleMember(em);
        et.commit();
    
        // 1차캐시에서 조회
        log.warn("find after transaction");
        findSimpleMember(em);
        
        em.clear();
        
        // DB에서 조회
        log.warn("find after clear");
        findSimpleMember(em);
    
        // DB에서 조회
        log.warn("find by new EntityManager");
        findSimpleMember(entityManagerFactory.createEntityManager());
    }
    
    public void findSimpleMember(EntityManager em) {
        SimpleMemberProduct memberProduct = em.find(SimpleMemberProduct.class, 1L);
        log.warn("order : {}", memberProduct.getId());
        log.warn("member : {}", memberProduct.getMember().getId());
        log.warn("product : {}", memberProduct.getProduct().getId());
        log.warn("orderAmount : {}", memberProduct.getOrderAmount());
    }
    
    
    public void save(EntityManager em, String memberId, String productId) {
        log.warn("save Start");
    
        SimpleProduct product = new SimpleProduct();
        product.setId(productId);
        product.setName("NAME_A");
        em.persist(product);
    
        SimpleMember member = new SimpleMember();
        member.setId(memberId);
        member.setUsername("USER_A");
        em.persist(member);
    
        SimpleMemberProduct memberProduct = new SimpleMemberProduct();
        memberProduct.setMember(member);
        memberProduct.setProduct(product);
        memberProduct.setOrderAmount(100);
        // 현재까지는 ID가 없는 상태
        em.persist(memberProduct);
        // persist이후 ID가 생성되어 들어간다
        log.warn("save persist complete");
    
        log.warn("save End");
    }
}
