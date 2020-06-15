package io.jistol.github.jpademo.entity.advanced;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jistol.github.jpademo.entity.advanced.entity.Book;
import io.jistol.github.jpademo.entity.advanced.entity.Item;
import io.jistol.github.jpademo.entity.advanced.entity.Movie;
import io.jistol.github.jpademo.entity.advanced.entity.QItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.stream.LongStream;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class ProxyIssueTest {
    @Autowired
    private SetupHelper advancedSetupHelper;

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @BeforeEach
    public void setup() {
        advancedSetupHelper.generateItem(10);
    }

    @Test
    @DisplayName("상속관계 엔티티의 프록시 클래스 테스트")
    @Transactional
    public void extendsEntityProxyTest() {
        EntityManager em1 = emf.createEntityManager();
        JPAQueryFactory query = new JPAQueryFactory(em1);
        query.selectFrom(QItem.item).fetch().stream()
                .map(item -> em.getReference(Item.class, item.getId()))
                .peek(this::print)
                .map(Item::getSelf)
                .forEach(this::print);
    }

    @Test
    @DisplayName("")
    public void extendsEntityProxyTest2() {
        LongStream.range(1L, 5L)
                .mapToObj(i -> em.getReference(Item.class, i))
                .peek(this::print)
                .map(Item::getSelf)
                .forEach(this::print);
    }

    private void print(Item item) {
        log.info("[{}]{} is item. {}", item.getId(), item.getName(), item.getClass().getName());
        if (item instanceof Book) {
            log.warn("{} is book. author : {}, {}", item.getId(), ((Book) item).getAuthor(), item.getClass().getName());
        } else if (item instanceof Movie) {
            log.warn("{} is movie. actor : {}, {}", item.getId(), ((Movie) item).getActor(), item.getClass().getName());
        } else {
            log.warn("{} is unknown... {}", item.getId(), item.getClass().getSimpleName());
        }
    }
}
