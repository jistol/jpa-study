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
@Transactional
@EntityScan("io.jistol.github.jpademo.entity.advanced")
@ActiveProfiles("advanced")
public class AdvancedInTransactionTest {
    @Autowired
    private SetupHelper advancedSetupHelper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        advancedSetupHelper.generate(15);
    }

    @Test
    @DisplayName("트랜잭션 범위 테스트")
    public void transactionTest() {
        log.warn("start transaction 1");
        BooleanExpression exp = QMember.member.name.eq("MEM1");
        Member mem1 = memberRepository.findOne(exp).get();
        Member mem2 = memberRepository.findOne(exp).get();

        log.warn("mem1 == mem2 : {}", mem1 == mem2);
        log.warn("end transaction 1");
    }
}
