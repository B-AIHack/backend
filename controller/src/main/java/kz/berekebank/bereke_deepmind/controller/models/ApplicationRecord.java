package kz.berekebank.bereke_deepmind.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplicationRecord(
  Long id,

  String contractNumber,
  ApplicationStatus status,

  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime createdAt

) {
}
