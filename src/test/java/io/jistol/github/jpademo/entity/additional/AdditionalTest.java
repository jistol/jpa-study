package io.jistol.github.jpademo.entity.additional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.additional.entity.Company;
import io.jistol.github.jpademo.entity.additional.entity.GoldMember;
import io.jistol.github.jpademo.entity.additional.entity.QGoldMember;
import io.jistol.github.jpademo.entity.additional.service.AdditionalTestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ActiveProfiles("additional")
@EntityScan("io.jistol.github.jpademo.entity.additional")
public class AdditionalTest {
    @Autowired
    private SetupHelper additaionalSetupHelper;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AdditionalTestService service;

    @BeforeEach
    public void setup() {
        additaionalSetupHelper.generate(50);
    }

    @Test
    @DisplayName("collection 테스트")
    public void collectionTest() {
        log.warn("start test");
        service.addGoldMember("GOLDMEM");
        log.warn("end test");
    }

    @Test
    @DisplayName("list 테스트")
    public void listTest() {
        log.warn("start test");
        service.addSilverMember("SILVERMEM");
        log.warn("end test");
    }

    @Test
    @DisplayName("set 테스트")
    public void setTest() {
        log.warn("start test");
        service.addBronzeMember("BRONZEMEM");
        log.warn("end test");
    }

    @Test
    @DisplayName("Convert 테스트")
    public void converterTest() {
        Company com1 = service.getCompany();
        log.warn("com1 - id : {}, isCommerce : {}", com1.getId(), com1.getIsCommerce());

        String query = "SELECT * FROM COMPANY WHERE IS_COMMERCE = 'Y'";
        List<Company> results = em.createNativeQuery(query, Company.class).getResultList();
        assertEquals(results.size(), 1);
        Company com2 = results.get(0);
        log.warn("com2 - id : {}, isCommerce : {}", com2.getId(), com2.getIsCommerce());
    }

    @Test
    @DisplayName("@NamedEntityGraph 테스트")
    public void namedEntityGraphTest() {
        log.warn("start select named entity graph!!!!");
        EntityGraph graph = em.getEntityGraph("GoldMember.withOrderInfos");
        Map hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", graph);
        Long id = service.getGoldMemberFirst().getId();
        GoldMember goldMember1 = em.find(GoldMember.class, id, hints);
        log.warn("id : {}, order size : {}", goldMember1.getId(), goldMember1.getOrderInfos().size());
        goldMember1.getOrderInfos().stream()
                .forEach(o -> log.warn("order id : {}", o.getId()));
        service.findOrderInfoByGoldMemberId(id).stream()
                .forEach(o -> log.warn("find order id : {}", o.getId()));
    }

    @Test
    @DisplayName("@NamedSubgraph 테스트")
    public void namedSubgraphTest() {
        log.warn("start!!!");
        EntityGraph graph = em.getEntityGraph("GoldMember.withAll");
        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        Long id = service.getGoldMemberFirst().getId();
        GoldMember goldMember1 = em.find(GoldMember.class, id, hints);
        log.warn("id : {}, order size : {}", goldMember1.getId(), goldMember1.getOrderInfos().size());
        goldMember1.getOrderInfos().stream()
                .peek(o -> log.warn("order id : {}", o.getId()))
                .flatMap(o -> o.getItems().stream())
                .forEach(i -> log.warn("item id : {}", i.getId()));
        service.findOrderInfoByGoldMemberId(id).stream()
                .peek(o -> log.warn("find order id : {}", o.getId()))
                .flatMap(o -> o.getItems().stream())
                .forEach(i -> log.warn("find item id : {}", i.getId()));
    }
}
