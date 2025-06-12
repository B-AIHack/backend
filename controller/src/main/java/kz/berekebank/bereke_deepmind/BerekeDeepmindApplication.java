package kz.berekebank.bereke_deepmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BerekeDeepmindApplication {

  public static void main(String[] args) {
    SpringApplication.run(BerekeDeepmindApplication.class, args);
  }

}
