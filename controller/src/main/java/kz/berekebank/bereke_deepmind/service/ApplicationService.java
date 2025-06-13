package kz.berekebank.bereke_deepmind.service;

import kz.berekebank.bereke_deepmind.controller.models.ApplicationFilter;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationRecord;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToCreate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToUpdate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationView;
import kz.berekebank.bereke_deepmind.controller.models.PageableResponse;
import kz.berekebank.bereke_deepmind.controller.models.ProcessDto;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

  Long create(ApplicationToCreate toSave);

  Long update(Long id, ApplicationToUpdate toUpdate);

  ApplicationView loadView(Long id);

  PageableResponse<ApplicationRecord> loadTable(ApplicationFilter applicationFilter, Pageable pageable);

  void process(Long id, ProcessDto processDto);

}
