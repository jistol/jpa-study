package io.jistol.github.jpademo.entity.jointable.onetomany;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.jointable.onetomany")
public class JoinTableOneToManyTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void oneToManyTest() {
        log.warn("Start");
        
        save();
        find();
        
        log.warn("End");
    }
    
    private void find() {
        EntityManager em = entityManagerFactory.createEntityManager();
        Parent parent = em.find(Parent.class, 1L);
        Child child = parent.getChilds().get(0);
        
        log.warn("parent is {}", parent.getName());
        log.warn("child is {}", child.getName());
        
    }
    
    private void save() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
    
        Parent parent = new Parent();
        parent.setName("PARENT");
        em.persist(parent);
    
        Child child = new Child();
        child.setName("CHILD");
        em.persist(child);
    
        parent.getChilds().add(child);
        
        et.commit();
        em.close();
    }
}
