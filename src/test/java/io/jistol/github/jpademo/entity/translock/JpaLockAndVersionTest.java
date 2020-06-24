package io.jistol.github.jpademo.entity.translock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.TestPack;
import io.jistol.github.jpademo.entity.translock.entity.Member;
import io.jistol.github.jpademo.entity.translock.entity.QMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.translock")
@ActiveProfiles("translock")
public class JpaLockAndVersionTest {
    @Autowired
    private SetupHelper translockSetupHelper;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @BeforeEach
    public void setup() {
        translockSetupHelper.generateMember(10);
    }

    @Test
    @DisplayName("@Version을 이용한 '최초커밋만 인정'모델 테스트")
    public void versionLockTest() {
        TestPack p1 = new TestPack(emf);
        TestPack p2 = new TestPack(emf);

        p1.et.begin();
        p2.et.begin();

        Member m1 = p1.em.find(Member.class, 2L);
        Member m2 = p2.em.find(Member.class, 2L);

        m1.setName("PACK1");
        m2.setName("PACK2");

        p2.em.persist(m2);
        p1.em.persist(m1);

        p1.et.commit();
        assertThrows(RollbackException.class, () -> p2.et.commit());
    }

    @Test
    @DisplayName("@Version 적용시점 테스트")
    public void versionUpdateTest() {
        log.warn("start case 1. flush/clear 없이 transaction.commit");
        TestPack t1 = new TestPack(emf);
        t1.et.begin();
        updateMember(t1);
        t1.et.commit();
        log.warn("end case 1.");

        log.warn("start case 2. flush/clear 이후 transaction.commit");
        TestPack t2 = new TestPack(emf);
        t2.et.begin();
        updateMember(t2);
        t2.em.flush();
        t2.em.clear();
        t2.et.commit();
        log.warn("end case 2.");

        log.warn("start case 3. flush 이후 transaction.commit");
        TestPack t3 = new TestPack(emf);
        t3.et.begin();
        updateMember(t3);
        t3.em.flush();
        t3.et.commit();
        log.warn("end case 3.");
    }

    public void updateMember(TestPack t) {
        Member m = t.em.find(Member.class, t.id);
        m.setName("UPDATE" + t.id);
    }

    @Test
    @DisplayName("JPA에서 락을 거는 테스트")
    public void jpaLockTest() {
        log.warn("start optimistic lock test");
        // EntityManager.find
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Member m1 = em.find(Member.class, 1L, LockModeType.NONE);

        // EntityManager.lock
        Member m2 = em.find(Member.class, 2L);
        em.lock(m2, LockModeType.OPTIMISTIC);

        // Query.setLockMode
        String query = "SELECT m FROM Member m WHERE id = 3";
        Member m3 = (Member)em.createQuery(query)
                .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                .getSingleResult();

        // @NamedQuery Lock
        Member m4 = (Member)em.createNamedQuery("Member.findByNameWithLock")
                .setParameter("name", "NAME4")
                .getSingleResult();

        et.commit();

        JPAQueryFactory jpa = new JPAQueryFactory(emf.createEntityManager());
        jpa.selectFrom(QMember.member)
                .where(QMember.member.id.lt(5L))
                .fetch()
                .stream()
                .forEach(m -> log.warn("ID : {}, NAME : {}, VERSION : {}", m.getId(), m.getName(), m.getVersion()));
    }
}
