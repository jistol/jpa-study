package io.jistol.github.jpademo.entity.additional;

import io.jistol.github.jpademo.entity.additional.entity.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component("additionalSetupHelper")
@Profile("additional")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    public Company genCompany(int i) {
        Company company = new Company();
        company.setName("COM" + i);
        company.setIsCommerce(Boolean.TRUE);
        return company;
    }

    public <T extends MemberBase> IntFunction<T> genMember(Supplier<T> constructor) {
        return i -> {
            T m = constructor.get();
            m.setName("MEM" + i);
            return m;
        };
    }

    public List<OrderInfo> genOrders(int cnt, GoldMember mem) {
        return IntStream.range(1, cnt)
                .mapToObj(i -> new OrderInfo("ORDER" + i))
                .peek(o -> o.setGoldMember(mem))
                .peek(o -> em.persist(o))
                .peek(o -> setupItem(3, o))
                .collect(Collectors.toList());
    }

    public void setupItem(int cnt, OrderInfo orderInfo) {
        IntStream.range(1, cnt)
                .mapToObj(i -> new Item("ITEM" + i))
                .peek(item -> item.setOrderInfo(orderInfo))
                .peek(em::persist)
                .forEach(item -> orderInfo.getItems().add(item));
    }

    @Transactional
    public void generate(int memberCount) {
        Company company = genCompany(0);
        em.persist(company);

        relate(memberCount, company, genMember(() -> new BronzeMember()), c -> c.getBronzes());
        relate(memberCount, company, genMember(() -> new SilverMember()), c -> c.getSilvers());
        List<GoldMember> members = relate(memberCount, company, genMember(() -> new GoldMember()), c -> c.getGolds());

        for (GoldMember mem : members) {
            genOrders(5, mem).stream().forEach(o -> mem.getOrderInfos().add(o));
        }
    }

    public <T extends MemberBase, C extends Collection<T>> List<T> relate(int count, Company company, IntFunction<T> memFun, Function<Company, C> sup) {
        return IntStream.range(0, count)
                .mapToObj(memFun)
                .peek(mem -> mem.setCompany(company))
                .peek(mem -> sup.apply(company).add(mem))
                .peek(mem -> em.persist(mem))
                .collect(Collectors.toList());
    }
}
