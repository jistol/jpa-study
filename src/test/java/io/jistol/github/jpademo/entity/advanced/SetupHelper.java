package io.jistol.github.jpademo.entity.advanced;

import io.jistol.github.jpademo.entity.advanced.entity.Member;
import io.jistol.github.jpademo.entity.advanced.entity.Product;
import io.jistol.github.jpademo.entity.advanced.entity.ProductDetail;
import io.jistol.github.jpademo.entity.advanced.entity.ProductItem;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component("advancedSetupHelper")
@Profile("advanced")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void generateMember(int memberCount) {
        IntStream.range(1, memberCount)
                .mapToObj(i -> new Member("MEM" + i))
                .forEach(m -> em.persist(m));
    }

    @Transactional
    public void generateProduct(int productCount) {
        IntStream.range(1, productCount)
                .mapToObj(i -> new Product())
                .peek(em::persist)
                .peek(p -> p.setName("PD" + p.getId()))
                .peek(this::setupProductDetail)
                .forEach(this::setupProductItem);
    }

    public void setupProductDetail(Product p) {
        ProductDetail detail = new ProductDetail();
        detail.setProduct(p);
        em.persist(detail);

        detail.setCount(RandomUtils.nextInt(1, 100));
        detail.setQuality("QUAL" + detail.getId());
        p.setProductDetail(detail);
    }

    public void setupProductItem(Product p) {
        IntStream.range(1, 5)
                .mapToObj(i -> new ProductItem())
                .peek(em::persist)
                .peek(it -> it.setName("ITEM" + it.getId()))
                .peek(it -> it.setProduct(p))
                .forEach(it -> p.getProductItems().add(it));
    }

}
