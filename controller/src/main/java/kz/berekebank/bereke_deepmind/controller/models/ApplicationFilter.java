package kz.berekebank.bereke_deepmind.controller.models;

import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;

import java.time.LocalDate;

public record ApplicationFilter(
  String contractNumber,
  ApplicationStatus status,
  LocalDate dateFrom,
  LocalDate dateTo
) {
}
