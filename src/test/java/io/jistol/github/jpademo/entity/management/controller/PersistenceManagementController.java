package io.jistol.github.jpademo.entity.management.controller;

import io.jistol.github.jpademo.entity.management.entity.Member;
import io.jistol.github.jpademo.entity.management.service.PersistenceManagementMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Profile("management")
@RestController
@RequestMapping("/management")
public class PersistenceManagementController {
    @Autowired
    private PersistenceManagementMemberService persistenceManagementMemberService;

    @PutMapping("/member/increase/age")
    public String increaseAge(@RequestParam("id") Member member) {
        log.warn("start increase age. member id : {}", member.getId());
        persistenceManagementMemberService.increaseAge(member);
        log.warn("increase complete.");
        member.setName("XXX"); // OSVI 미사용시 지연로딩으로 인한 오류 발생
        log.warn("end, return member info with masking name.");
        return member.getTeam().getName() + "," + member.getName();
    }
}
