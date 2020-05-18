package io.jistol.github.jpademo.entity.valuetype.collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.valuetype.collection")
public class CollectionTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void collectionTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Member member = new Member();
        member.setHomeAddress(generateAddress(0));
        member.setFavoriteFoods(IntStream.range(1, 10).mapToObj(i -> "FOOD" + i).collect(Collectors.toSet()));
        member.setAddressHistory(IntStream.range(1, 10).mapToObj(i -> generateAddress(i)).collect(Collectors.toList()));
        em.persist(member);
        
        et.commit();
    }
    
    private Address generateAddress(int seq) {
        Address address = new Address();
        address.setCity("CITY" + seq);
        address.setStreet("STREET" + seq);
        address.setZipcode("1111" + seq);
        return address;
    }
}
