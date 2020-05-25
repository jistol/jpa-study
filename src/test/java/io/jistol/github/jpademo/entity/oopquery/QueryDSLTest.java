package io.jistol.github.jpademo.entity.oopquery;

import com.querydsl.core.Tuple;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import io.jistol.github.jpademo.entity.oopquery.entity.QMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@SpringBootTest
@Slf4j
@EntityScan("io.jistol.github.jpademo.entity.oopquery")
public class QueryDSLTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private SetupHelper setupHelper;

    @BeforeEach
    public void setup() {
        setupHelper.generate(25);
    }

    @Test
    @DisplayName("QueryDSL 기본테스트")
    public void queryDSLTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = QMember.member;

        List<Member> members = query.selectFrom(qMember)
                //.where(qMember.name.eq("MEM1"), qMember.age.gt(15)) // and 연산
                .where(qMember.name.eq("MEM1").or(qMember.age.gt(15)))
                .orderBy(qMember.name.desc())
                .fetch();

        members.stream().forEach(mem -> log.warn("mem.name : {}, age : {}", mem.getName(), mem.getAge()));
    }

    @Test
    @DisplayName("QueryDSL 페이징/정렬 테스트")
    public void pagingAndOrderingTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qm = QMember.member;

        List<Tuple> results = query.from(qm)
                .select(qm.name, qm.age)
                .where(qm.age.gt(10))
                .orderBy(qm.age.desc(), qm.name.desc())
                .offset(2)
                .limit(3)
                .fetch();

        results.stream().forEach(tuple -> log.warn("name : {}, age : {}", tuple.get(qm.name), tuple.get(qm.age)));
    }
}
