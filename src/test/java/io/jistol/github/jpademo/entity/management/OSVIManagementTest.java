package io.jistol.github.jpademo.entity.management;

import io.jistol.github.jpademo.entity.management.entity.Member;
import io.jistol.github.jpademo.entity.management.repository.PersistenceManagementRespository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("management")
@EntityScan("io.jistol.github.jpademo.entity.management")
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
public class OSVIManagementTest {
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
    @DisplayName("OSVI 테스트")
    public void queryMethodPersistenceTest() throws Exception {
        List<Member> members = persistenceManagementRespository.findAll();
        long id = members.get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/management/member/increase/age?id=" + id))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print());
    }
}
