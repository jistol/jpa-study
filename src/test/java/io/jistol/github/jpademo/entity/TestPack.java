package io.jistol.github.jpademo.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class TestPack {
    private static final AtomicLong sequence = new AtomicLong(1L);
    public final Long id;
    public final EntityManager em;
    public final EntityTransaction et;
    public final CountDownLatch latch;

    public TestPack(EntityManagerFactory emf) {
        id = sequence.getAndIncrement();
        em = emf.createEntityManager();
        et = em.getTransaction();
        latch = new CountDownLatch(1);
    }
}
