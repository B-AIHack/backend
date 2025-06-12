package kz.berekebank.bereke_deepmind.repository.specification;

import kz.berekebank.bereke_deepmind.controller.models.ApplicationFilter;
import kz.berekebank.bereke_deepmind.repository.models.Application;
import org.springframework.data.jpa.domain.Specification;

import java.time.temporal.ChronoUnit;

public class ApplicationSpecification extends AbstractSpecification<Application> {

  public static Specification<Application> pageSpecification(ApplicationFilter filter) {
    return new ApplicationSpecification().construct(filter);
  }

  private Specification<Application> construct(ApplicationFilter filter) {

    var spec = empty();

    spec = spec.and(where(Application.Fields.deleted, false));

    if (filter == null) {
      return spec;
    }

    if (filter.contractNumber() != null) {
      spec = spec.and(whereLike(Application.Fields.contractNumber, filter.contractNumber()));
    }
    if (filter.status() != null) {
      spec = spec.and(where(Application.Fields.status, filter.status()));
    }

    if (filter.dateFrom() != null) {
      spec = spec.and(from(Application.Fields.createdAt, filter.dateFrom()));
    }

    if (filter.dateTo() != null) {
      spec = spec.and(to(Application.Fields.createdAt, filter.dateTo().plus(1, ChronoUnit.DAYS)));
    }

    return spec;

  }

}
