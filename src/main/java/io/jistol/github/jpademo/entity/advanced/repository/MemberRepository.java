package io.jistol.github.jpademo.entity.advanced.repository;

import io.jistol.github.jpademo.entity.advanced.entity.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

@Profile("advanced")
public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
}
