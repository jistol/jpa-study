package io.jistol.github.jpademo.entity.springdatajpa;

import io.jistol.github.jpademo.entity.oopquery.entity.QMember;
import io.jistol.github.jpademo.entity.springdatajpa.entity.Address;
import io.jistol.github.jpademo.entity.springdatajpa.entity.Member;
import io.jistol.github.jpademo.entity.springdatajpa.repository.SpringDataJpaMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("springdatajpa")
@EntityScan("io.jistol.github.jpademo.entity.springdatajpa")
public class SpringDataJpaTest {
    @Autowired
    private SetupHelper springdatajpaSetupHelper;

    @Autowired
    private SpringDataJpaMemberRepository springDataJpaMemberRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setup() {
        springdatajpaSetupHelper.generate(25);
    }

    @Test
    @DisplayName("쿼리메소드 기본 테스트")
    public void queryMethodTest() {
        List<Member> allMems = springDataJpaMemberRepository.findAll();
        Address address = allMems.get(0).getAddress();
        List<Member> members = springDataJpaMemberRepository.findByAddressCityAndAddressZipcode(address.getCity(), address.getZipcode());
        members.stream().forEach(mem -> log.warn("city : {}, name : {}, age : {}", mem.getAddress().getCity(), mem.getName(), mem.getAddress().getZipcode()));
    }

    @Test
    @DisplayName("NamedQuery 테스트")
    public void namedQueryTest() {
        List<Member> allMems = springDataJpaMemberRepository.findAll();
        Member mem = allMems.get(0);
        List<Member> members = springDataJpaMemberRepository.findByInfo(mem.getName(), mem.getAge());
        members.stream().forEach(this::print);
    }

    @Test
    @DisplayName("@Query 테스트")
    public void queryAnnotationTest() {
        springDataJpaMemberRepository.findByAgeByQuery(15).stream().forEach(this::print);
        springDataJpaMemberRepository.findByAgeByNativeQuery(15).stream().forEach(this::print);
    }

    private void print(Member m) {
        log.warn("name : {}, age : {}", m.getName(), m.getAge());
    }

    @Test
    @DisplayName("쿼리메소드 영속성 테스트")
    public void queryMethodPersistenceTest() {
        Member mem1 = springDataJpaMemberRepository.findAll(QMember.member.age.ne(10)).iterator().next();
        Member mem2 = springDataJpaMemberRepository.findById(mem1.getId()).get();
        log.warn("mem1 == mem2 : {}", mem1 == mem2);  // false
    }
}
