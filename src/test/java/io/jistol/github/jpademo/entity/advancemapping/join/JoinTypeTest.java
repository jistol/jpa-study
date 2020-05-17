package io.jistol.github.jpademo.entity.advancemapping.join;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advancemapping.join")
public class JoinTypeTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void joinTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Album album = new Album();
        album.setArtist("KIM");
        em.persist(album);
        
        Movie movie = new Movie();
        movie.setActor("LEE");
        movie.setDirector("PACK");
        em.persist(movie);
        
        Book book = new Book();
        book.setAuthor("YOO");
        book.setIsbn("1235-2424-2424-4444");
        em.persist(book);
        
        et.commit();
    }
    
    @Test
    public void joinNonDiscriminatorTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        FlatBook book = new FlatBook();
        book.setAuthor("KIM");
        em.persist(book);
        
        et.commit();
    }
}
