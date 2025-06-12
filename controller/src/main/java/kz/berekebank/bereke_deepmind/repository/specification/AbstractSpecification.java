package kz.berekebank.bereke_deepmind.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class AbstractSpecification<T> {

  protected static String WILDCARD = "%";

  protected Specification<T> from(@SuppressWarnings("SameParameterValue") String dateFieldName, LocalDate date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(dateFieldName), date);
  }

  protected Specification<T> to(@SuppressWarnings("SameParameterValue") String dateFieldName, LocalDate date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(dateFieldName), date);
  }

  protected Specification<T> from(@SuppressWarnings("SameParameterValue") String dateFieldName, LocalDateTime date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(dateFieldName), date);
  }

  protected Specification<T> to(@SuppressWarnings("SameParameterValue") String dateFieldName, LocalDateTime date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(dateFieldName), date);
  }

  protected Specification<T> where(String fieldName, Object value) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), value);
  }

  protected Specification<T> whereLike(@SuppressWarnings("SameParameterValue") String fieldName, String value) {
    return (root, query, cb) ->
      cb.like(cb.lower(root.get(fieldName)), WILDCARD + value.toLowerCase() + WILDCARD);
  }

  protected Specification<T> empty() {
    return Specification.where(null);
  }

  protected Specification<T> whereGreaterThanOrEqualTo(@SuppressWarnings("SameParameterValue") String fieldName, BigDecimal amountFrom) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), amountFrom);
  }

  protected Specification<T> whereGreaterThanOrEqualTo(@SuppressWarnings("SameParameterValue") String fieldName, LocalDate date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), date);
  }

  protected Specification<T> whereLessThanOrEqualTo(@SuppressWarnings("SameParameterValue") String fieldName, BigDecimal amountTo) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), amountTo);
  }

  protected Specification<T> whereLessThanOrEqualTo(@SuppressWarnings("SameParameterValue") String fieldName, LocalDate date) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), date);
  }

  protected static String like(String text) {
    return WILDCARD + text + WILDCARD;
  }

}
