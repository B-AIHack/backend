package kz.berekebank.bereke_deepmind.feign.models.python_service;

public record ProcessResponse(
  ProcessResult result,
  String error,
  String result_raw
) {
}
