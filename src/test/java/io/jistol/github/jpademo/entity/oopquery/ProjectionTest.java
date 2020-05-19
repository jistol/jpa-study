package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.SetupHelper;
import io.jistol.github.jpademo.entity.oopquery.entity.Address;
import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class ProjectionTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private SetupHelper setupHelper;
    
    @BeforeEach
    public void init() {
       setupHelper.generate(10);
    }
    
    @Test
    public void entityProjectionTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        // member 조회 / team 조회 => SQL 2번
        int age = 3;
        String query = "SELECT m FROM Member m WHERE m.age = :age";
        List<Member> results = em.createQuery(query, Member.class)
                .setParameter("age", age)
                .getResultList();
        log.warn("select complete m.age = {}", age);
        log.warn("m.age = 3 : {}", results.size());
    }
    
    @Test
    public void embeddedTypeProjectionTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        // member만 조회 (team 조회 안함), embedded된 컬럼만 조회됨
        int age = 3;
        String query = "SELECT m.address FROM Member m WHERE m.age = :age";
        List<Address> results = em.createQuery(query, Address.class)
                .setParameter("age", age)
                .getResultList();
    
        log.warn("select complete m.age = {}", age);
        log.warn("m.age = 3 : {}", results.size());
    }
    
    @Test
    public void scalaTypeProjectionTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        int age = 3;
        String query = "SELECT name FROM Member m WHERE m.age = :age";
        List<String> results = em.createQuery(query, String.class)
                .setParameter("age", age)
                .getResultList();
        
        log.warn("select complete m.age = {}", age);
        log.warn("m.age = 3 : {}", results.size());
        
    }
}
