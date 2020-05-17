package io.jistol.github.jpademo.entity.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnitUtil;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.proxy")
public class ProxyTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void proxyGetIdTest() {
        Long id = save().getId();
        EntityManager em = entityManagerFactory.createEntityManager();
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Member m = em.getReference(Member.class, id);
        log.warn("member id is {}", m.getId());
        log.warn("member is initialized : {}", util.isLoaded(m));
        log.warn("member name is {}", m.getName());
        log.warn("member's team name is {}", m.getTeam().getName());
        log.warn("member is initialized : {}", util.isLoaded(m));
        log.warn("order 0's amount is {}", m.getOrders().get(0).getAmount());
        log.warn("order 1's id is {}", m.getOrders().get(1).getId());
        
        m.getOrders().remove(0);
        log.warn("remove order 0");
        et.commit();
        
        
    }
    
    @Test
    public void proxyTest() {
        Long id = save().getId();
        find(id);
        remove(id);
    }
    
    private void remove(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        log.warn("remove start");
        et.begin();
        
        em.remove(em.getReference(Member.class, id));
        
        et.commit();
        
        log.warn("remove end");
        
    }
    
    private void find(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        log.warn("find Start");
        Member m = em.getReference(Member.class, id);
        log.warn("after getReference");
        log.warn("m is Member : {}", m.getClass().getName());
        Member m2 = em.getReference(Member.class, id);
        
        log.warn("member name is {}", m.getName());
        log.warn("m is Member : {}", m.getClass().getName());
        log.warn("m2 is Member : {}", m2.getClass().getName());
    
        Member m3 = em.getReference(Member.class, id);
        log.warn("m3 is Member : {}", m3.getClass().getName());
    
        EntityManager em2 = entityManagerFactory.createEntityManager();
        Member m4 = em2.find(Member.class, id);
        log.warn("m4 is Member : {}", m4.getClass().getName());
        Member m5 = em2.find(Member.class, id);
        log.warn("m5 is Member : {}", m5.getClass().getName());
        
        log.warn("find complete");
    }
    
    private Member save() {
        Team t = saveTeam();
        
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Member m = new Member();
        m.setName("Member1");
        m.setTeam(em.getReference(Team.class, t.getId()));
        em.persist(m);
    
        for (int i=0 ; i<10 ; i++) {
            Order order = new Order();
            order.setAmount(i);
            order.setName("ORDER" + i);
            order.setMember(m);
            m.getOrders().add(order);
        }
        
        et.commit();
        return m;
    }
    
    private Team saveTeam() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Team t = new Team();
        t.setName("TEAM1");
        em.persist(t);
    
        et.commit();
        return t;
    }
}
