package io.jistol.github.jpademo.entity.additional.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.additional.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.QueryHints;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Profile("additional")
public class AdditionalTestService {
    @PersistenceContext
    private EntityManager em;

    public Company getCompany() {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectFrom(QCompany.company).limit(1).fetchOne();
    }

    @Transactional
    public GoldMember getGoldMemberFirst() {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectFrom(QGoldMember.goldMember).fetchFirst();
    }

    @Transactional
    public void findByEntityGraphHintInSameTransaction() {
        log.warn("Start findByEntityGraphHintInSameTransaction.");
        EntityGraph graph = em.getEntityGraph("GoldMember.withOrderInfos");
        Map hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        Long id = getGoldMemberFirst().getId();
        GoldMember goldMember1 = em.find(GoldMember.class, id, hints);
        log.warn("id : {}, order size : {}", goldMember1.getId(), goldMember1.getOrderInfos().size());
        goldMember1.getOrderInfos().stream().forEach(o -> log.warn("order id : {}", o.getId()));
        findOrderInfoByGoldMemberId(id).stream().forEach(o -> log.warn("find order id : {}", o.getId()));
        log.warn("End findByEntityGraphHintInSameTransaction.");
    }

    @Transactional
    public List<OrderInfo> findOrderInfoByGoldMemberId(Long id) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectFrom(QOrderInfo.orderInfo)
                .join(QOrderInfo.orderInfo.items, QItem.item)
                .fetchJoin()
                .where(QOrderInfo.orderInfo.goldMember.id.eq(id))
                .fetch();
    }

    @Transactional
    public void addGoldMember(String name) {
        Company company = getCompany();
        log.warn("query Company");

        GoldMember m = new GoldMember();
        m.setName(name);
        m.setCompany(company);
        em.persist(m);
        log.warn("persist {}", name);

        company.getGolds().add(m);
        log.warn("add new GoldMember.");
    }

    @Transactional
    public void addSilverMember(String name) {
        Company company = getCompany();
        log.warn("query Company");

        SilverMember m = new SilverMember();
        m.setName(name);
        m.setCompany(company);
        em.persist(m);
        log.warn("persist {}", name);

        company.getSilvers().add(m);
        log.warn("add new SilverMember.");
    }
    @Transactional
    public void addBronzeMember(String name) {
        Company company = getCompany();
        log.warn("query Company");

        BronzeMember m = new BronzeMember();
        m.setName(name);
        m.setCompany(company);
        em.persist(m);
        log.warn("persist {}", name);

        company.getBronzes().add(m);
        log.warn("add new BronzeMember.");
    }
}
