package io.jistol.github.jpademo.entity.management.repository;

import io.jistol.github.jpademo.entity.management.entity.Member;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

@Profile("management")
public interface PersistenceManagementRespository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

}
