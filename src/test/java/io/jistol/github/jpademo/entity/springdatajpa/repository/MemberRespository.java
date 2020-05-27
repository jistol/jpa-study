package io.jistol.github.jpademo.entity.springdatajpa.repository;

import io.jistol.github.jpademo.entity.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRespository extends JpaRepository<Member, Long> {
    List<Member> findByAddressCityAndAddressZipcode(String city, String zipcode);

    List<Member> findByInfo(@Param("name") String name, @Param("age") int age);

    @Query("SELECT m FROM Member m WHERE m.age = ?1")
    List<Member> findByAgeByQuery(int age);

    @Query(value = "SELECT * FROM MEMBER WHERE age = ?1", nativeQuery = true)
    List<Member> findByAgeByNativeQuery(int age);
}
