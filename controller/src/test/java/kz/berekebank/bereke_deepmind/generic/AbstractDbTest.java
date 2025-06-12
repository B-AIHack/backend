package kz.berekebank.bereke_deepmind.generic;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@Slf4j
@AutoConfigureEmbeddedDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.cloud.vault.enabled=false"})
@ActiveProfiles("test")
public abstract sealed class AbstractDbTest permits AbstractJpaTest, AbstractJdbcTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public EasyRandomParameters parameters;
  public EasyRandom           randomizer;

  public static final Random rnd = new Random();

  @BeforeEach
  void init() {

    parameters = new EasyRandomParameters()
      .charset(StandardCharsets.UTF_8)
      .seed(rnd.nextLong())
      .objectPoolSize(100);

    randomizer = new EasyRandom(parameters);

    cleanDB();

  }

  private void cleanDB() {
    final String query      = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
    List<String> tableNames = jdbcTemplate.queryForList(query, String.class);
    log.info("List database for cleaning {}", tableNames);

    for (String tableName : tableNames) {
      jdbcTemplate.update("TRUNCATE TABLE ? RESTART IDENTITY CASCADE", tableName);
      log.info("Truncate {} table", tableName);
    }
  }

}
