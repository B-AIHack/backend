package kz.berekebank.bereke_deepmind.feign;

import kz.berekebank.bereke_deepmind.feign.config.FeignConfig;
import kz.berekebank.bereke_deepmind.feign.models.postman_echo.PostmanEchoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PostmanEchoClient", configuration = FeignConfig.class, url = "${integration.host}")
public interface PostmanEchoClient {

  @GetMapping("/get")
  PostmanEchoResponse get(@RequestParam("id") String id, @RequestParam("text") String text);

}
