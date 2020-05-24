package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import io.jistol.github.jpademo.entity.oopquery.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@SpringBootTest
@Slf4j
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class CriteriaTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private SetupHelper setupHelper;

    @BeforeEach
    public void setup() {
        setupHelper.generate(10);
    }

    @Test
    @DisplayName("criteria select 테스트")
    public void selectTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaQuery<Member> cq = em.getCriteriaBuilder().createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);
        cq.select(m);
        log.warn("selected criteria!!!");

        TypedQuery<Member> query = em.createQuery(cq);
        List<Member> members = query.getResultList();
        log.warn("size : {}", members.size());
    }

    @Test
    @DisplayName("ORDER BY / WHERE 추가하기")
    public void orderAndWhereTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);

        Predicate nameEquals = cb.equal(m.get("name"), "회원1");
        Order ageDesc = cb.desc(m.get("age"));

        cq.select(m)
                .where(nameEquals)
                .orderBy(ageDesc);

        List<Member> results = em.createQuery(cq).getResultList();
        log.warn("results size : {}", results.size());
    }

    @Test
    @DisplayName("10살초과 하는 회원 조회")
    public void selectGraterThanAge10() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = builder.createQuery(Member.class);
        Root<Member> m = query.from(Member.class);

        Predicate ageGt = builder.greaterThan(m.<Integer>get("age"), 10);
        query.select(m).where(ageGt).orderBy(builder.desc(m.get("age")));

        List<Member> members = em.createQuery(query).getResultList();
        log.warn("member size : {}", members.size());
    }

    @Test
    @DisplayName("튜플 조회 테스트")
    public void tupleSelectTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery(); // eq : cb.createQuery(Tuple.class);
        Root<Member> m = cq.from(Member.class);
        Predicate where = cb.greaterThan(m.<Integer>get("age"), 10);
        cq.multiselect(
                m.get("name").alias("n"),
                m.get("age").alias("a")
        ).where(where);

        List<Tuple> tuples = em.createQuery(cq).getResultList();
        tuples.stream().forEach(tuple -> {
            log.warn("name : {}, age : {}", tuple.get("n", String.class), tuple.get("a", Integer.class));
        });
    }

    /**
     * SELECT m, t FROM Member m
     * INNER JOIN m.team t
     * WHERE t.name = 'TEAMA'
     */
    @Test
    @DisplayName("join 테스트")
    public void joinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Member> m = cq.from(Member.class);
        Join<Member, Team> t = m.join("team", JoinType.INNER);
        cq.multiselect(m.alias("m"), t.alias("t")).where(cb.equal(t.get("name"), "TEAM1"));
        List<Tuple> tuples = em.createQuery(cq).getResultList();
        tuples.stream().forEach(tuple -> {
            Member mem = tuple.get("m", Member.class);
            Team team = tuple.get("t", Team.class);
            log.warn("mem.name : {}, team.name : {}", mem.getName(), team.getName());
        });
    }

    @Test
    @DisplayName("Fetch Join 테스트")
    public void fetchJoinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);
        Root<Member> m = cq.from(Member.class);
        m.fetch("team", JoinType.LEFT);
        cq.select(m).where(cb.equal(m.get("team").get("name"), "TEAM1"));

        List<Member> members = em.createQuery(cq).getResultList();
        members.stream().forEach(mem -> log.warn("mem.name : {}, team.name : {}", mem.getName(), mem.getTeam().getName()));
    }
}
