package io.jistol.github.jpademo.entity.advanced;

import io.jistol.github.jpademo.entity.advanced.entity.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.stream.IntStream;

@Component("advancedSetupHelper")
@Profile("advanced")
public class SetupHelper {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void generate(int memberCount) {
        IntStream.range(1, memberCount)
                .mapToObj(i -> new Member("MEM" + i))
                .forEach(m -> em.persist(m));
    }

}
