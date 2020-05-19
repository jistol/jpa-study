package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class PagingTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private SetupHelper setupHelper;
    
    @BeforeEach
    public void setup() {
        setupHelper.generate(50);
    }
    
    @Test
    public void pagingTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
    
        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m ORDER BY m.name DESC", Member.class);
        
        List<Member> results = query.setFirstResult(5)
                .setMaxResults(5)
                .getResultList();
        log.warn("Select Complete.");
        results.stream().forEach(m -> log.warn("name : {}", m.getName()));
    }
}
