package io.jistol.github.jpademo.entity.management.service;

import io.jistol.github.jpademo.entity.management.entity.Member;
import io.jistol.github.jpademo.entity.management.repository.PersistenceManagementRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Profile("management")
public class PersistenceManagementMemberService {
    @Autowired
    private PersistenceManagementRespository persistenceManagementRespository;

    @Transactional
    public void increaseAge(Member member) {
        member.setAge(member.getAge() + 1);
        persistenceManagementRespository.save(member);
    }
}
