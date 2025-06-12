package kz.berekebank.bereke_deepmind.controller.models;

public record UploadFileResponse(
  Long id,
  String filename,
  String contentType
) {
}
