package kz.berekebank.bereke_deepmind;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestPropertySource(properties = {"spring.cloud.vault.enabled=false"})
@ActiveProfiles("test")
class MbTemplateApplicationTest  {
//  @Test
//  void smokeTest() {
//    assertTrue(true);
//  }
}
