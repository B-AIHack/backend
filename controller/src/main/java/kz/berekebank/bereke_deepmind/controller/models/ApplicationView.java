package kz.berekebank.bereke_deepmind.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApplicationView(
  Long id,

  String contractNumber,
  ApplicationStatus status,

  List<UploadFileResponse> uploadedFiles,

  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime createdAt,
  boolean deleted
) {
}
