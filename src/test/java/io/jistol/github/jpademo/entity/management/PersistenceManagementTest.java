package io.jistol.github.jpademo.entity.management;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Slf4j
@EntityScan("io.jistol.github.jpademo.entity.management")
public class PersistenceManagementTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SetupHelper managementSetupHelper;

    @BeforeEach
    public void setup() {
        managementSetupHelper.generate(30);
    }

}
