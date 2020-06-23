package io.jistol.github.jpademo.entity.advanced.service;

import io.jistol.github.jpademo.entity.advanced.entity.Book;
import io.jistol.github.jpademo.entity.advanced.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Slf4j
@Service
public class BatchTestService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void batch(int count, int cutPosition) {
        execute(em, count,cutPosition);
    }

    public void execute(EntityManager em, int count, int cutPosition) {
        for (int i = 0 ; i < count ; i++) {
            Member m = new Member();
            m.setName("MEM" + i);
            em.persist(m);
            if (cutPosition > 0 && i >= cutPosition && i % cutPosition == 0) {
                Book b = new Book();
                b.setName("BOOK" + i);
                b.setIsbn("ISBN" + i);
                em.persist(b);
            }
        }
    }
}
