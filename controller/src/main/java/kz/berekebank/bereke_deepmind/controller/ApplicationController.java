package kz.berekebank.bereke_deepmind.controller;

import kz.berekebank.bereke_deepmind.controller.models.ApplicationFilter;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationRecord;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToCreate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToUpdate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationView;
import kz.berekebank.bereke_deepmind.controller.models.PageableResponse;
import kz.berekebank.bereke_deepmind.controller.models.ProcessDto;
import kz.berekebank.bereke_deepmind.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/application")
public class ApplicationController {

  private final ApplicationService applicationService;

  @PostMapping
  public Long create(@RequestBody ApplicationToCreate toCreate) {
    return applicationService.create(toCreate);
  }

  @PostMapping("/{id}")
  public Long update(@PathVariable("id") Long id,
                     @RequestBody ApplicationToUpdate toUpdate) {
    return applicationService.update(id, toUpdate);
  }

  @PostMapping("/{id}")
  public void process(@PathVariable("id") Long id,
                      @RequestBody ProcessDto processDto) {
    applicationService.process(id, processDto);
  }

  @GetMapping("/{id}")
  public ApplicationView loadView(@PathVariable("id") Long id) {
    return applicationService.loadView(id);
  }

  @GetMapping("/page")
  public PageableResponse<ApplicationRecord> loadTable(ApplicationFilter applicationFilter,
                                                       @ParameterObject @PageableDefault Pageable pageable) {
    return applicationService.loadTable(applicationFilter, pageable);
  }


}
