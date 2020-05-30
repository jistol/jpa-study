package io.jistol.github.jpademo.entity.additional;

import io.jistol.github.jpademo.entity.additional.entity.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Component("additionalSetupHelper")
@Profile("additional")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    public Company genCompany(int i) {
        Company company = new Company();
        company.setName("COM" + i);
        return company;
    }

    public <T extends MemberBase> IntFunction<T> genMember(Supplier<T> constructor) {
        return i -> {
            T m = constructor.get();
            m.setName("MEM" + i);
            return m;
        };
    }

    @Transactional
    public void generate(int memberCount) {
        Company company = genCompany(0);
        em.persist(company);

        relate(memberCount, company, genMember(() -> new BronzeMember()), c -> c.getBronzes());
        relate(memberCount, company, genMember(() -> new SilverMember()), c -> c.getSilvers());
        relate(memberCount, company, genMember(() -> new GoldMember()), c -> c.getGolds());
    }

    public <T extends MemberBase, C extends Collection<T>> void relate(int count, Company company, IntFunction<T> memFun, Function<Company, C> sup) {
        IntStream.range(0, count)
                .mapToObj(memFun)
                .peek(mem -> mem.setCompany(company))
                .peek(mem -> sup.apply(company).add(mem))
                .forEach(mem -> em.persist(mem));
    }
}
