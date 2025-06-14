package kz.berekebank.bereke_deepmind.feign.models.python_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ComplianceValidation(
  String rule,
  @JsonProperty("matched_text")
  String matchedText,
  List<String> references
) {
}
