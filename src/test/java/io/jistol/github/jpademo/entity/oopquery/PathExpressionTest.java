package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class PathExpressionTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private SetupHelper setupHelper;
    
    @BeforeEach
    public void setup() {
        setupHelper.generate(20);
    }
    
    @Test
    public void singleAssociationTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
    
        Stream.of(
                "SELECT t.name FROM Member m JOIN m.team t",
                "SELECT m.team.name FROM Member m",
                "SELECT m.name FROM Member m WHERE m.team.name = 'TEAM0'"
        )
                .map(query -> em.createQuery(query, String.class))
                .map(typed -> typed.getResultList())
                .forEach(res -> log.warn("{}", res));
    }
    
    @Test
    public <T extends Class<List<Member>>> void collectionAssociationTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        List members = em.createQuery("SELECT t.members FROM Team t").getResultList();
        log.warn("res1 : {}", ((Member)members.get(0)).getName());
        
        List<String> res2 = em.createQuery("SELECT m.name FROM Team t JOIN t.members m", String.class).getResultList();
        log.warn("res2 : {}", res2);
        
        // @Deprecated
        log.warn("size 1 : {}", em.createQuery("SELECT t.members.size FROM Team t", Integer.class).getResultList());
        // recommended
        List list = em.createQuery("SELECT size(t.members) FROM Team t", Integer.class).getResultList();
    }
}
