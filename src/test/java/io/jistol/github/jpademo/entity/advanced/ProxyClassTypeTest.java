package io.jistol.github.jpademo.entity.advanced;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.advanced.entity.Product;
import io.jistol.github.jpademo.entity.advanced.entity.QProduct;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.QueryHints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class ProxyClassTypeTest {
    @Autowired
    private SetupHelper advancedSetupHelper;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @BeforeEach
    public void setup() {
        advancedSetupHelper.generateProduct(10);
    }

    @Test
    @DisplayName("proxy class 비교 테스트")
    public void compareProxyClass() {
        log.warn("start findOne.");
        EntityManager em1 = emf.createEntityManager();
        Product p1 = findOne(em1);
        log.warn("end findOne.");
        log.warn("p1.detail.class : {}", p1.getProductDetail().getClass().getName());

        EntityManager em = emf.createEntityManager();
        log.warn("start find by Hint");
        Product p2 = em.find(Product.class, p1.getId(), getHint("Product.withDetail", em));
        log.warn("end find by Hint");
        log.warn("p2.detail.class : {}", p2.getProductDetail().getClass().getName());
    }

    public Product findOne(EntityManager em) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectFrom(QProduct.product).limit(1).fetchOne();
    }

    public Map getHint(String hint, EntityManager em) {
        Map map = new HashMap();
        map.put(QueryHints.FETCHGRAPH, em.getEntityGraph(hint));
        return map;
    }
}
