package kz.berekebank.bereke_deepmind.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI openApi() {
    Info info = new Info()
      .title("Micro Service Back End template Service API")
      .version("1.0")
      .description("This API exposes endpoints to file storage operations.");

    return new OpenAPI().info(info);
  }
}
