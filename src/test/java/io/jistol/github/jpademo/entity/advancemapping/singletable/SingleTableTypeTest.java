package io.jistol.github.jpademo.entity.advancemapping.singletable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advancemapping.singletable")
public class SingleTableTypeTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void singleTableTest() {
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
}
