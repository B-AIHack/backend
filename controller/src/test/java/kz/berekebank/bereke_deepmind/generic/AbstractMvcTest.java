package kz.berekebank.bereke_deepmind.generic;

import kz.berekebank.security.config.SecurityAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.cloud.vault.enabled=false")
@AutoConfigureMockMvc
@ImportAutoConfiguration(SecurityAutoConfiguration.class)
public abstract class AbstractMvcTest {

  @Autowired
  protected MockMvc mockMvc;
}
