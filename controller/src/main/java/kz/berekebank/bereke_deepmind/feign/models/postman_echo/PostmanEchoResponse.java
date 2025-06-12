package kz.berekebank.bereke_deepmind.feign.models.postman_echo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record PostmanEchoResponse(
  Map<String, String> args,
  Headers headers
) {

  public record Headers(
    String host,
    @JsonProperty("x-request-start")
    String xRequestStartTime
  ) {}

}
