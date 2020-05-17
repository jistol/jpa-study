package io.jistol.github.jpademo.entity.jointable.secondary;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.jointable.secondary")
public class JoinTableSecondaryTableTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void manyToOneTest() {
        log.warn("Start");
        
        Board board = save();
        find(board.getId());
        
        log.warn("End");
    }
    
    private void find(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Board board = em.find(Board.class, id);
        log.warn("title : {}", board.getTitle());
        log.warn("content : {}", board.getContent());
    }
    
    private Board save() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
    
        Board board = new Board();
        board.setTitle("BOARD_TITLE");
        board.setContent("BOARD_CONTENT");
        em.persist(board);
        
        et.commit();
        em.close();
        
        return board;
    }
}
