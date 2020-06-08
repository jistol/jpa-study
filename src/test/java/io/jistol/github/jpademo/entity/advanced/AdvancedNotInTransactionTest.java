package io.jistol.github.jpademo.entity.advanced;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.jistol.github.jpademo.entity.advanced.entity.Member;
import io.jistol.github.jpademo.entity.advanced.entity.QMember;
import io.jistol.github.jpademo.entity.advanced.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@Slf4j
@SpringBootTest
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class AdvancedNotInTransactionTest {
    @Autowired
    private SetupHelper advancedSetupHelper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        advancedSetupHelper.generateMember(15);
    }

    @Test
    @DisplayName("트랜잭션 범위 테스트")
    public void transactionTest() {
        BooleanExpression exp = QMember.member.name.eq("MEM1");
        Member mem1 = memberRepository.findOne(exp).get();
        Member mem2 = memberRepository.findOne(exp).get();

        log.warn("mem1 == mem2 : {}", mem1 == mem2);
        log.warn("mem1 equals mem2 : {}", mem1.equals(mem2));
        log.warn("mem1.id == mem2.id : {}", mem1.getId() == mem2.getId());
    }

}
