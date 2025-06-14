package kz.berekebank.bereke_deepmind.feign.models.python_service;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProcessTextDto (
  @JsonProperty("file_text")
  String fileText
){
}
