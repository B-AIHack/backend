package kz.berekebank.bereke_deepmind.feign.models.python_service;

public record ProcessResult(
  String contractNumber,
  String contractDate,
  String buyer,
  String seller,
  String operationType,
  String contractAmount,
  String currency,
  String repatriationTerm,
  String counterpartyName,
  String counterpartyCountry,
  String counterpartyBank,
  String buyerInn,
  String sellerInn
) {
}
