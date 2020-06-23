package io.jistol.github.jpademo.entity.advanced;

import io.jistol.github.jpademo.entity.advanced.repository.MemberRepository;
import io.jistol.github.jpademo.entity.advanced.service.BatchTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class JDBCBatchTest {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BatchTestService batchTestService;

    @Test
    public void batchBySpringTransactionTest() {
        batchTestService.batch(10, 7);
    }

    @Test
    public void batchBySpringRepositoryTest() {

    }

    @Test
    public void batchTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        batchTestService.execute(em, 10, 7);
        et.commit();
    }
}
