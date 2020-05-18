package io.jistol.github.jpademo.entity.valuetype.embedded;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;

@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.valuetype.embedded")
public class EmbeddedTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void embeddedMemberTest() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        Member member = new Member();
        member.setName("MEMBER");
        
        Period period = new Period();
        period.setStartDate(LocalDate.now());
        period.setEndDate(LocalDate.now().plusDays(2));
        member.setWorkPeriod(period);
        
        Zipcode zipcode = new Zipcode();
        zipcode.setZip("123");
        zipcode.setPlusFour("54");
        
        Address address = new Address();
        address.setCity("Seoul");
        address.setStreet("Jung-go");
        address.setZipcode(zipcode);
        member.setHomeAddress(address);
        member.setCompanyAddress(address);
        
        PhoneNumberProvider phoneNumberProvider = new PhoneNumberProvider();
        phoneNumberProvider.setName("KIM");
        em.persist(phoneNumberProvider);
        
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setAreaCode("02");
        phoneNumber.setLocalNumber("1234-1234");
        phoneNumber.setProvider(phoneNumberProvider);
        member.setPhoneNumber(phoneNumber);
        
        em.persist(member);
        
        et.commit();
    }
}
