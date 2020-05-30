package io.jistol.github.jpademo.entity.additional;

import io.jistol.github.jpademo.entity.additional.service.AdditionalTestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@SpringBootTest
@ActiveProfiles("additional")
@EntityScan("io.jistol.github.jpademo.entity.additional")
public class AdditionalTest {
    @Autowired
    private SetupHelper additaionalSetupHelper;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AdditionalTestService service;

    @BeforeEach
    public void setup() {
        additaionalSetupHelper.generate(50);
    }

    @Test
    @DisplayName("collection 테스트")
    public void collectionTest() {
        log.warn("start test");
        service.addGoldMember("GOLDMEM");
        log.warn("end test");
    }

    @Test
    @DisplayName("list 테스트")
    public void listTest() {
        log.warn("start test");
        service.addSilverMember("SILVERMEM");
        log.warn("end test");
    }

    @Test
    @DisplayName("set 테스트")
    public void setTest() {
        log.warn("start test");
        service.addBronzeMember("BRONZEMEM");
        log.warn("end test");
    }
}
