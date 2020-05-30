package io.jistol.github.jpademo.entity.additional.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.additional.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Slf4j
@Service
@Profile("additional")
public class AdditionalTestService {
    @PersistenceContext
    private EntityManager em;

    private Company getCompany() {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectFrom(QCompany.company).limit(1).fetchOne();
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
