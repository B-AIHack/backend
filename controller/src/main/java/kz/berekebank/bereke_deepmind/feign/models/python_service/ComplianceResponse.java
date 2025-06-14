package kz.berekebank.bereke_deepmind.feign.models.python_service;

import java.util.List;

public record ComplianceResponse(
  List<String> complianceValidationList
) {
}
