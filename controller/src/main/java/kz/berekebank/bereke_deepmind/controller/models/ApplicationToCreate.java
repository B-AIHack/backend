package kz.berekebank.bereke_deepmind.controller.models;

import java.util.List;

public record ApplicationToCreate(
  String contractNumber,
  List<Long> uploadedFileIds
) {
}
