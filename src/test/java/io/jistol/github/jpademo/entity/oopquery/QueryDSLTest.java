package io.jistol.github.jpademo.entity.oopquery;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import io.jistol.github.jpademo.entity.oopquery.dto.MemTeamDTO;
import io.jistol.github.jpademo.entity.oopquery.entity.Member;
import io.jistol.github.jpademo.entity.oopquery.entity.QMember;
import io.jistol.github.jpademo.entity.oopquery.entity.QTeam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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

    @Test
    @DisplayName("조인 테스트")
    public void joinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);

        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        List<Member> members = query.selectFrom(qMember)
                .join(qMember.team, qTeam)
                .where(qTeam.name.eq("TEAM1"))
                .limit(10)
                .fetch();

        members.stream().forEach(mem -> log.warn("mem.name : {}, team : {}", mem.getName(), mem.getTeam().getName()));
    }

    @Test
    @DisplayName("조인 ON 테스트")
    public void joinOnTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);

        List<Member> members = query.selectFrom(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .on(QTeam.team.name.eq("TEAM1"))
                .fetch();

        members.stream().forEach(mem -> log.warn("mem.name : {}, team : {}", mem.getName(), mem.getTeam().getName()));
    }

    @Test
    @DisplayName("서브쿼리 테스트")
    public void subqueryTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);

        JPQLQuery<Long> subquery = JPAExpressions.select(QTeam.team.id)
                .from(QTeam.team)
                .where(QTeam.team.name.eq("TEAM1").or(QTeam.team.name.eq("TEAM2")));

        long total = query.selectFrom(QMember.member)
                .where(QMember.member.team.id.in(subquery))
                .fetchResults()
                .getTotal();

        log.warn("result total size : {}", total);
    }

    @Test
    @DisplayName("프로젝션 bean 테스트")
    public void projectionBeanTest() {
        QMember qm = new QMember("qm");
        QBean<MemTeamDTO> bean = Projections.bean(MemTeamDTO.class, qm.name.as("memberName"), qm.team.name.as("teamName"));
        projectionTest(bean);
    }

    @Test
    @DisplayName("프로젝션 field 테스트")
    public void projectionFieldTest() {
        QMember qm = new QMember("qm");
        QBean<MemTeamDTO> bean = Projections.fields(MemTeamDTO.class, qm.name.as("memberName"), qm.team.name.as("teamName"));
        projectionTest(bean);
    }

    @Test
    @DisplayName("프로젝션 constructor 테스트")
    public void projectionConstructorTest() {
        QMember qm = new QMember("qm");
        ConstructorExpression<MemTeamDTO> exp = Projections.constructor(MemTeamDTO.class, qm.name.as("memberName"), qm.team.name.as("teamName"));
        projectionTest(exp);
    }

    private void projectionTest(Expression<MemTeamDTO> selector) {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qm = new QMember("qm");
        QTeam qt = new QTeam("qt");
        List<MemTeamDTO> res = query.select(selector)
                .from(qm)
                .join(qm.team, qt)
                .where(qt.name.eq("TEAM1"))
                .fetch();

        res.stream().forEach(dto -> log.warn("memName : {}, teamName : {}", dto.getMemberName(), dto.getTeamName()));
    }

    @Test
    @DisplayName("JPAUpdateClause 수정 배치 쿼리 테스트")
    public void batchUpdateClauseTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAUpdateClause updateClause = new JPAUpdateClause(em, QMember.member);
        EntityTransaction et = em.getTransaction();
        et.begin();
        long count = updateClause.where(QMember.member.age.gt(10))
                .set(QMember.member.name, QMember.member.name.append("XXX"))
                .execute();
        et.commit();
        log.warn("update count : {}", count);

        selectForUpdateResult("XXX");
    }

    @Test
    @DisplayName("일반 수정 테스트")
    public void updateTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();

        JPAQueryFactory query = new JPAQueryFactory(em);
        long count = query.update(QMember.member)
                .where(QMember.member.age.gt(20))
                .set(QMember.member.name, QMember.member.name.append("XXX"))
                .execute();

        log.warn("update count : {}", count);
        et.commit();

        selectForUpdateResult("XXX");
    }

    private void selectForUpdateResult(String suffix) {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);
        List<Member> members = query.selectFrom(QMember.member)
                .where(QMember.member.name.endsWith(suffix))
                .fetch();

        members.stream().forEach(mem -> log.warn("name : {}, age : {}", mem.getName(), mem.getAge()));
    }

    @Test
    @DisplayName("Builder를 통한 동적쿼리")
    public void dynamicQueryByBuilder() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);

        BooleanBuilder builder = new BooleanBuilder();
        if (RandomUtils.nextBoolean()) {
            builder.and(QMember.member.age.gt(15));
        }

        if (RandomUtils.nextBoolean()) {
            builder.and(QMember.member.age.lt(20));
        }

        if (RandomUtils.nextBoolean()) {
            builder.and(QMember.member.age.ne(18));
        }

        List<Member> members = query.selectFrom(QMember.member).where(builder).fetch();
        members.stream().forEach(mem -> log.warn("name : {}, age : {}", mem.getName(), mem.getAge()));
    }

    @Test
    @DisplayName("메소드 위임 테스트")
    public void methodDelegateTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em);
        List<Member> members = query.selectFrom(QMember.member)
                .where(QMember.member.isOldAge())
                .fetch();

        members.stream().forEach(mem -> log.warn("age : {}", mem.getAge()));

    }
}
