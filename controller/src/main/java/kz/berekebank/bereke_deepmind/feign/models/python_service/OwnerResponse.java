package kz.berekebank.bereke_deepmind.feign.models.python_service;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OwnerResponse(
  @JsonProperty("ФИО")
  String fio,
  @JsonProperty("ИНН")
  String iin,
  @JsonProperty("Доля (руб)")
  String sum
) {
}
