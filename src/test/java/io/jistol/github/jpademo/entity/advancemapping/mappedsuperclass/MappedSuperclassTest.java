package io.jistol.github.jpademo.entity.advancemapping.mappedsuperclass;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advancemapping.mappedsuperclass")
public class MappedSuperclassTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void mappedSuperclassTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Member member = new Member();
        member.setMemberName("MEM");
        em.persist(member);
        
        User user = new User();
        user.setUsername("USER");
        em.persist(user);
        
        et.commit();
    }
    
    
}
