package io.jistol.github.jpademo.entity.management;

import io.jistol.github.jpademo.entity.management.entity.Member;
import io.jistol.github.jpademo.entity.management.repository.PersistenceManagementRespository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = "/osvi.yml")
@ActiveProfiles("management")
@EntityScan("io.jistol.github.jpademo.entity.management")
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
public class NonOSVIManagementTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SetupHelper managementSetupHelper;

    @Autowired
    private PersistenceManagementRespository persistenceManagementRespository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        managementSetupHelper.generate(30);
    }

    @Test
    @DisplayName("OSVI 테스트 - 영속성 제거로 인한 지연로딩 오류")
    public void layzLoadingExceptionTest() {
        List<Member> members = persistenceManagementRespository.findAll();
        long id = members.get(0).getId();
        assertThrows(NestedServletException.class,
                () -> mockMvc.perform(put("/management/member/increase/age?id=" + id)));
    }
}
