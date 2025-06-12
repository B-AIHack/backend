package kz.berekebank.bereke_deepmind.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@Slf4j
public class TestController {

  @GetMapping("/public")
  public void publicEndpoint() {
    log.info("hit public endpoint");
  }

  @GetMapping("/private")
  public void privateEndpoint() {
    log.info("hit private endpoint");
  }
}
