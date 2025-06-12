package kz.berekebank.bereke_deepmind.service.impl;

import kz.berekebank.bereke_deepmind.controller.models.ApplicationFilter;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationRecord;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToCreate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToUpdate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationView;
import kz.berekebank.bereke_deepmind.controller.models.PageableResponse;
import kz.berekebank.bereke_deepmind.controller.models.UploadFileResponse;
import kz.berekebank.bereke_deepmind.repository.ApplicationRepository;
import kz.berekebank.bereke_deepmind.repository.UploadedFileRepository;
import kz.berekebank.bereke_deepmind.repository.models.Application;
import kz.berekebank.bereke_deepmind.repository.models.UploadedFile;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import kz.berekebank.bereke_deepmind.repository.specification.ApplicationSpecification;
import kz.berekebank.bereke_deepmind.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final ApplicationRepository  applicationRepository;
  private final UploadedFileRepository uploadedFileRepository;

  @Override
  @Transactional
  public Long create(ApplicationToCreate toCreate) {

    if (toCreate.contractNumber() == null) {
      throw new RuntimeException("CONTRACT_NUMBER_IS_NULL");
    }
    if (CollectionUtils.isEmpty(toCreate.uploadedFileIds())) {
      throw new RuntimeException("UPLOADED_FILES_IS_EMPTY");
    }

    if (applicationRepository.existsByContractNumber(toCreate.contractNumber())) {
      throw new RuntimeException("CONTRACT_ID_ALREADY_EXIST");
    }

    List<UploadedFile> uploadedFileIds = loadUploadedFiles(toCreate.uploadedFileIds());

    Application application = Application.builder()
                                         .status(ApplicationStatus.NEW)
                                         .contractNumber(toCreate.contractNumber())
                                         .files(uploadedFileIds)
                                         .createdAt(LocalDateTime.now())
                                         .deleted(false)
                                         .build();

    return applicationRepository.save(application).getId();

  }

  private List<UploadedFile> loadUploadedFiles(List<Long> ids) {
    List<UploadedFile> uploadedFiles = uploadedFileRepository.findAllById(ids);

    if (CollectionUtils.isEmpty(ids)) {
      throw new RuntimeException("UPLOADED_FILES_NOT_EXIST");
    }

    if (uploadedFiles.stream().anyMatch(x -> x.getApplication() != null)) {
      throw new RuntimeException("FILE_ALREADY_ASSIGNED_TO_OTHER_APPLICATION");
    }
    return uploadedFiles;
  }

  @Override
  @Transactional
  public Long update(Long id, ApplicationToUpdate toUpdate) {

    Application application = findApplication(id);

    List<UploadedFile> uploadedFileIds = loadUploadedFiles(toUpdate.uploadedFileIds());

    application.setFiles(uploadedFileIds);

    return applicationRepository.save(application).getId();

  }

  @Override
  @Transactional(readOnly = true)
  public ApplicationView loadView(Long id) {
    final Application application = findApplication(id);

    return mapToView(application);
  }

  @Override
  public PageableResponse<ApplicationRecord> loadTable(ApplicationFilter filter, Pageable pageable) {

    Specification<Application> spec = ApplicationSpecification.pageSpecification(filter);

    final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                                   Sort.by(Sort.Direction.DESC, Application.Fields.createdAt));

    Page<Application> all = applicationRepository.findAll(spec, pageRequest);

    Page<ApplicationRecord> page = all.map(this::mapToRecord);

    return PageableResponse.of(page);

  }

  private ApplicationRecord mapToRecord(Application application) {
    return ApplicationRecord.builder()
                            .id(application.getId())
                            .contractNumber(application.getContractNumber())
                            .status(application.getStatus())
                            .createdAt(application.getCreatedAt())
                            .build();
  }

  private ApplicationView mapToView(Application application) {
    return ApplicationView.builder()
                          .id(application.getId())
                          .contractNumber(application.getContractNumber())
                          .status(application.getStatus())
                          .uploadedFiles(application.getFiles().stream()
                                                    .map(x -> new UploadFileResponse(x.getId(),
                                                                                     x.getFilename(),
                                                                                     x.getContentType()))
                                                    .collect(Collectors.toList()))
                          .createdAt(application.getCreatedAt())
                          .deleted(application.isDeleted())
                          .build();
  }

  private Application findApplication(Long id) {
    return applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("NO_APPLICATION_WITH_ID"));
  }

}
