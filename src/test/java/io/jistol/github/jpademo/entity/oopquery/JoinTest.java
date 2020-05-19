package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import io.jistol.github.jpademo.entity.oopquery.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class JoinTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private SetupHelper setupHelper;
    
    @BeforeEach
    public void setup() {
        setupHelper.generate(50);
    }
    
    @Test
    public void joinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        String query = "SELECT DISTINCT t FROM Member m JOIN m.team t";
        List<Team> teams = em.createQuery(query, Team.class).getResultList();
        
        log.warn("team size is {}", teams.size());
    }
    
    @Test
    public void collectionJoinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        String query = "SELECT DISTINCT m FROM Team t LEFT JOIN t.members m";
        List<Member> members = em.createQuery(query, Member.class).getResultList();
        
        log.warn("team size is {}", members.size());
    }
    
    @Test
    public void entityFetchJoinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        
        String query = "SELECT m FROM Member m JOIN FETCH m.team";
        List<Member> members = em.createQuery(query, Member.class).getResultList();
        
        log.warn("team size is {}", members.size());
    }
}
