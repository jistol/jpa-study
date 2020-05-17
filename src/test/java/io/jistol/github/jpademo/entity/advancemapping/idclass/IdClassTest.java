package io.jistol.github.jpademo.entity.advancemapping.idclass;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advancemapping.idclass")
public class IdClassTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void idClassTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Parent parent = new Parent();
        parent.setId1("ID1_1");
        parent.setId2("ID2_1");
        parent.setName("PARENT");
        em.persist(parent);
        
        Child child = new Child();
        child.setParent(parent);
        em.persist(child);
        
        et.commit();
    }
}
