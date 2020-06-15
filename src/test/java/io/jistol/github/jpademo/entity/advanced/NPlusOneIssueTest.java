package io.jistol.github.jpademo.entity.advanced;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.HasId;
import io.jistol.github.jpademo.entity.advanced.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class NPlusOneIssueTest {
    @Autowired
    private SetupHelper advancedSetupHelper;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("즉시로딩 타입 테스트")
    @Transactional
    public void eagerTest() {
        advancedSetupHelper.generateEagerParent(10);
        EntityManager em1 = emf.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em1);
        log.warn("조회시 자동으로 부모/자식 객체를 join하여 같이 조회");
        query.selectFrom(QEagerParent.eagerParent).fetch().stream()
                .map(EagerParent::getId)
                .map(id -> em.find(EagerParent.class, id))
                .forEach(parent -> log.warn("id : {}, child size : {}", parent.getId(), parent.getChildList().size()));
    }

    @Test
    @DisplayName("JPQL을 이용한 즉시로딩 테스트")
    @Transactional
    public void eagerByJPQLTest() {
        advancedSetupHelper.generateEagerParent(10);
        String query = "SELECT p FROM EagerParent p";
        log.warn("start query");
        List<EagerParent> list = em.createQuery(query, EagerParent.class).setMaxResults(3).getResultList();
        log.error("N+1이슈 발생 - 부모객체만 조회했으나 eager 타입으로 설정되어 있어 자식객체를 각각 조회 (ID기반).");
        log.warn("end query");
        EagerParent parent = list.get(0);
        log.warn("id : {}, child size : {}", parent.getId(), parent.getChildList().size());
    }


    @Test
    @DisplayName("@BatchSize + JPQL을 이용한 즉시로딩 테스트")
    @Transactional
    public void batchEagerByJPQLTest() {
        advancedSetupHelper.generateBatchEagerParent(20);
        batchSizeRunner(BatchEagerParent.class, 7);
    }

    @Test
    @DisplayName("@BatchSize + JPQL을 이용한 지연로딩 테스트")
    @Transactional
    public void batchLazyByJPQLTest() {
        advancedSetupHelper.generateBatchLazyParent(40);
        batchSizeRunner(BatchLazyParent.class, 11);
    }

    @Test
    @DisplayName("@Fetch + JPQL을 이용한 즉시로딩 테스트")
    @Transactional
    public void fetchEagerByJPQLTest() {
        advancedSetupHelper.generateFetchEagerParent(20);
        batchSizeRunner(FetchEagerParent.class, 7);
    }

    private <P extends HasId & HasChild> void batchSizeRunner(Class<P> parentClazz, int loadSize) {
        String query = "SELECT p FROM " + parentClazz.getSimpleName() + " p";
        log.warn("start query");
        List<P> list = em.createQuery(query, parentClazz).getResultList();
        log.warn("end query");
        list.stream().limit(loadSize)
                .forEach(parent -> log.warn("id : {}, child size : {}", parent.getId(), parent.getChildList().size()));
    }
}
