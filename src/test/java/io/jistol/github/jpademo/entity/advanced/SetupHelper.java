package io.jistol.github.jpademo.entity.advanced;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.HasParent;
import io.jistol.github.jpademo.entity.NameSupport;
import io.jistol.github.jpademo.entity.advanced.entity.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.function.Supplier;
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateItem(int count) {
        IntStream.range(1, count)
                .mapToObj(i -> i % 2 == 0 ? makeBook(i) : makeMovie(i))
                .forEach(em::persist);
    }

    public Item makeBook(int i) {
        Book book = new Book();
        book.setAuthor("AUTHOR" + i);
        book.setIsbn(Integer.toString(1000 + i));
        book.setName("BOOK" + i);
        return book;
    }

    public Item makeMovie(int i) {
        Movie movie = new Movie();
        movie.setActor("ACTOR" + i);
        movie.setName("MOVIE" + i);
        return movie;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateEagerParent(int count) {
        IntStream.range(1, count)
                .mapToObj(this::makeParent)
                .forEach(parent -> {
                    IntStream.range(1, 5)
                            .mapToObj(this::makeChild)
                            .peek(child -> child.setEagerParent(parent))
                            .forEach(child -> parent.getChildList().add(child));
                });
    }

    public EagerParent makeParent(int seq) {
        EagerParent parent = new EagerParent();
        parent.setName("PARENT" + seq);
        em.persist(parent);
        return parent;
    }

    public EagerChild makeChild(int seq) {
        EagerChild child = new EagerChild();
        child.setName("CHILD" + seq);
        em.persist(child);
        return child;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateBatchEagerParent(int count) {
        generateParentChild(count, () -> new BatchEagerParent(), () -> new BatchEagerChild());
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateBatchLazyParent(int count) {
        generateParentChild(count, () -> new BatchLazyParent(), () -> new BatchLazyChild());
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateFetchEagerParent(int count) {
        generateParentChild(count, () -> new FetchEagerParent(), () -> new FetchEagerChild());
    }

    private <P extends HasChild<C> & NameSupport, C extends HasParent<P> & NameSupport> void generateParentChild(int count, Supplier<P> supParent, Supplier<C> supChild) {
        IntStream.range(1, count)
                .mapToObj(i -> {
                    P parent = supParent.get();
                    parent.setName("PARENT" + i);
                    em.persist(parent);
                    return parent;
                })
                .forEach(parent -> {
                    IntStream.range(1, 5)
                            .mapToObj(i -> {
                                C child = supChild.get();
                                child.setName("CHILD" + i);
                                em.persist(child);
                                return child;
                            })
                            .peek(child -> child.setParent(parent))
                            .forEach(child -> parent.getChildList().add(child));
                });
    }
}
