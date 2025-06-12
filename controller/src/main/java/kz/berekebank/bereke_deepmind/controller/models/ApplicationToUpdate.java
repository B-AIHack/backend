package kz.berekebank.bereke_deepmind.controller.models;

import java.util.List;

public record ApplicationToUpdate(
  List<Long> uploadedFileIds
) {
}
