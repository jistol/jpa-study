package io.jistol.github.jpademo.entity.relate;

import io.jistol.github.jpademo.entity.relate.entity.Member;
import io.jistol.github.jpademo.entity.relate.entity.Order;
import io.jistol.github.jpademo.entity.relate.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import javax.transaction.Transactional;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.relate")
@Transactional
public class ProxyTest {
    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void proxyGetIdTest() {
        Long id = save().getId();
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();
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
    }

    @Test
    public void proxyTest() {
        Long id = save().getId();
        find(id);
        remove(id);
    }
    
    private void remove(Long id) {
        log.warn("remove start");
        em.remove(em.getReference(Member.class, id));
        log.warn("remove end");
    }
    
    private void find(Long id) {
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
        return m;
    }
    
    private Team saveTeam() {
        Team t = new Team();
        t.setName("TEAM1");
        em.persist(t);
        return t;
    }
}
