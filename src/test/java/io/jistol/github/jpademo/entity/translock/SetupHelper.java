package io.jistol.github.jpademo.entity.translock;

import io.jistol.github.jpademo.entity.HasChild;
import io.jistol.github.jpademo.entity.HasParent;
import io.jistol.github.jpademo.entity.NameSupport;
import io.jistol.github.jpademo.entity.translock.entity.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Component("translockSetupHelper")
@Profile("translock")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void generateMember(int memberCount) {
        Function<Integer, Member> gen = i -> {
            Member m = new Member();
            m.setName("NAME" + i);
            return m;
        };

        IntStream.range(1, memberCount)
                .mapToObj(gen::apply)
                .forEach(m -> em.persist(m));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateParentChild(int parentCount) {
        genParentChild(parentCount, () -> new EagerParent(), () -> new EagerChild());
        genParentChild(parentCount, () -> new LazyParent(), () -> new LazyChild());
    }

    private <C extends HasParent<P> & NameSupport, P extends HasChild<C> & NameSupport> void genParentChild(int parentCount, Supplier<P> supParent, Supplier<C> supChild) {
        IntStream.rangeClosed(1, parentCount)
                .mapToObj(i -> gen(supParent, i))
                .peek(em::persist)
                .forEach(p -> {
                    IntStream.rangeClosed(1, 3)
                            .mapToObj(i -> gen(supChild, i))
                            .peek(c -> c.setParent(p))
                            .peek(em::persist)
                            .forEach(c -> p.getChildList().add(c));
                });
    }

    private <T extends NameSupport> T gen(Supplier<T> sup, int seq) {
        T t = sup.get();
        t.setName("NAME" + seq);
        return t;
    }




}
