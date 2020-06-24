package io.jistol.github.jpademo.entity.translock;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.TestPack;
import io.jistol.github.jpademo.entity.translock.entity.EagerChild;
import io.jistol.github.jpademo.entity.translock.entity.EagerParent;
import io.jistol.github.jpademo.entity.translock.entity.QEagerParent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceUnit;
import java.util.function.Consumer;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.translock")
@ActiveProfiles("translock")
public class OptimisticForceIncreamentTest {
    @Autowired
    private SetupHelper translockSetupHelper;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @BeforeEach
    public void setup() {
        translockSetupHelper.generateParentChild(3);
    }

    @Test
    @DisplayName("OPTIMISTIC_FORCE_INCREMENT 테스트 - Eager, Child를 로딩하여 수정하는 케이스")
    public void eager_by_child_optimisticForceIncrementTest() {
        // Child의 Version만 2로 수정되고 종료
        execute(p1 -> {
            EagerChild c1 = p1.em.find(EagerChild.class, 2L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            c1.setName("UPDATE_CHILD_NAME!!!");
        });
    }

    @Test
    @DisplayName("OPTIMISTIC_FORCE_INCREMENT 테스트 - Eager, Child를 로딩하여 Parent를 수정하는 케이스")
    public void eager_upparent_by_child_optimisticForceIncrementTest() {
        // Parent / Child가 각각 1씩 수정되고 종료
        execute(p1 -> {
            EagerChild c1 = p1.em.find(EagerChild.class, 2L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            c1.getParent().setName("UPDATE_PARENT_NAME!!!");
        });
    }

    @Test
    @DisplayName("OPTIMISTIC_FORCE_INCREMENT 테스트 - Eager, parent를 로딩하여 수정하는 케이스")
    public void eager_by_parent_optimisticForceIncrementTest() {
        // Parent의 Version만 2로 수정되고 종료
        execute(p1 -> {
            EagerParent c1 = p1.em.find(EagerParent.class, 1L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            c1.setName("UPDATE_PARENT_NAME!!!");
        });
    }

    @Test
    @DisplayName("OPTIMISTIC_FORCE_INCREMENT 테스트 - Eager, parent를 로딩하여 Child를 수정하는 케이스")
    public void eager_upchild_by_parent_optimisticForceIncrementTest() {
        // Parent와 Child 1,2 의 Version이 1로 변경되고 종료
        execute(p1 -> {
            EagerParent parent = p1.em.find(EagerParent.class, 1L, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            parent.getChildList().get(0).setName("UPDATE_CHILD_1");
            parent.getChildList().get(1).setName("UPDATE_CHILD_2");
            EagerChild c3 = parent.getChildList().get(2);
            log.debug("c3 = {}", c3.getName());
        });
    }

    public void execute(Consumer<TestPack> consumer) {
        TestPack p1 = new TestPack(emf);
        p1.et.begin();
        consumer.accept(p1);
        p1.et.commit();
        printAll();
    }

    private void printAll() {
        JPAQueryFactory query = new JPAQueryFactory(emf.createEntityManager());
        query.selectFrom(QEagerParent.eagerParent)
                .fetch()
                .stream()
                .peek(p -> log.warn("id : {}, name : {}, version : {}", p.getId(), p.getName(), p.getVersion()))
                .flatMap(p -> p.getChildList().stream())
                .forEach(c -> log.warn(">> id : {}, name : {}, version : {}", c.getId(), c.getName(), c.getVersion()));
    }
}
