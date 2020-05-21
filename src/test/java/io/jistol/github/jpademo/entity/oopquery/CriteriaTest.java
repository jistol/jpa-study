package io.jistol.github.jpademo.entity.oopquery;

import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
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

}
