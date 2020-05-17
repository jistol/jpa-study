package io.jistol.github.jpademo.entity.jointable.manytoone;

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
@EntityScan("io.jistol.github.jpademo.entity.jointable.manytoone")
public class JoinTableManyToOneTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void manyToOneTest() {
        log.warn("Start");
        
        Child child = save();
        find(child.getId());
        
        log.warn("End");
    }
    
    private void find(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Child child = em.find(Child.class, id);
        Parent parent = child.getParent();
        
        log.warn("parent is {}", parent.getName());
        log.warn("child is {}", child.getName());
        
    }
    
    private Child save() {
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
        child.setParent(parent);
        
        et.commit();
        em.close();
        
        return child;
    }
}
