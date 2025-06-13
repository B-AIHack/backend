package kz.berekebank.bereke_deepmind.service.impl;

import kz.berekebank.bereke_deepmind.controller.models.ApplicationFilter;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationRecord;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToCreate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationToUpdate;
import kz.berekebank.bereke_deepmind.controller.models.ApplicationView;
import kz.berekebank.bereke_deepmind.controller.models.PageableResponse;
import kz.berekebank.bereke_deepmind.controller.models.ProcessDto;
import kz.berekebank.bereke_deepmind.controller.models.UploadFileResponse;
import kz.berekebank.bereke_deepmind.feign.PythonClient;
import kz.berekebank.bereke_deepmind.feign.models.python_service.OwnerResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessResult;
import kz.berekebank.bereke_deepmind.repository.ApplicationRepository;
import kz.berekebank.bereke_deepmind.repository.PersonsRepository;
import kz.berekebank.bereke_deepmind.repository.UploadedFileRepository;
import kz.berekebank.bereke_deepmind.repository.models.Application;
import kz.berekebank.bereke_deepmind.repository.models.UploadedFile;
import kz.berekebank.bereke_deepmind.repository.models.enums.ApplicationStatus;
import kz.berekebank.bereke_deepmind.repository.specification.ApplicationSpecification;
import kz.berekebank.bereke_deepmind.service.ApplicationService;
import kz.berekebank.bereke_deepmind.util.multipart.CustomMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final ApplicationRepository  applicationRepository;
  private final UploadedFileRepository uploadedFileRepository;
  private final PersonsRepository      personsRepository;

  private final PythonClient pythonClient;

  private final RestTemplate restTemplate;

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

  @Override
  @Transactional
  public void process(Long id, ProcessDto processDto) {

    final Application application = findApplication(id);

    if (processDto.process()) {
      process(application);
    } else if (processDto.findOwners()) {
      findOwners(application);
    }

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

                          .contractDate(application.getContractDate())
                          .buyer(application.getBuyer())
                          .seller(application.getSeller())
                          .operationType(application.getOperationType())
                          .contractAmount(application.getContractAmount())
                          .currency(application.getCurrency())
                          .repatriationTerm(application.getRepatriationTerm())
                          .counterpartyName(application.getCounterpartyName())
                          .counterpartyCountry(application.getCounterpartyCountry())
                          .counterpartyBank(application.getCounterpartyBank())
                          .buyerInn(application.getBuyerInn())
                          .sellerInn(application.getSellerInn())

                          .createdAt(application.getCreatedAt())
                          .deleted(application.isDeleted())
                          .build();
  }

  private Application findApplication(Long id) {
    return applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("NO_APPLICATION_WITH_ID"));
  }

  public void findOwners(Application application) {

    final List<OwnerResponse> owners = pythonClient.findOwners(application.getSellerInn());

    if (CollectionUtils.isEmpty(owners)) {
      return;
    }

    boolean denied = false;
    for (final OwnerResponse owner : owners) {

      final boolean contains = personsRepository.contains(owner.fio());

      if (contains) {
        denied = true;
        continue;
      }

    }

    if (denied) {
      application.setStatus(ApplicationStatus.DENIED);
      applicationRepository.save(application);
    } else {

      application.setStatus(ApplicationStatus.APPROVED);
      applicationRepository.save(application);
    }

  }

  public void process(Application application) {

    application.setStatus(ApplicationStatus.PROCESSING);

    for (final UploadedFile file : application.getFiles()) {

      if (file.isChecked()) {
        continue;
      }

      final CustomMultipartFile multipartFile = new CustomMultipartFile(file.getFilename(), file.getFilename(),
                                                                        file.getContentType(), file.getData());

      final ProcessResponse process = process(multipartFile);

//      final ProcessResponse process = pythonClient.process(multipartFile);

      if (process == null || process.error() != null || process.result() == null) {
        break;
      }

      final ProcessResult result = process.result();

      if (application.getContractDate() == null) {
        application.setContractDate(result.contractDate());
      }
      if (application.getBuyer() == null) {
        application.setBuyer(result.buyer());
      }
      if (application.getSeller() == null) {
        application.setSeller(result.seller());
      }
      if (application.getOperationType() == null) {
        application.setOperationType(result.operationType());
      }
      if (application.getContractAmount() == null) {
        application.setContractAmount(result.contractAmount());
      }
      if (application.getCurrency() == null) {
        application.setCurrency(result.currency());
      }
      if (application.getRepatriationTerm() == null) {
        application.setRepatriationTerm(result.repatriationTerm());
      }
      if (application.getCounterpartyName() == null) {
        application.setCounterpartyName(result.counterpartyName());
      }
      if (application.getCounterpartyCountry() == null) {
        application.setCounterpartyCountry(result.counterpartyCountry());
      }
      if (application.getCounterpartyBank() == null) {
        application.setCounterpartyBank(result.counterpartyBank());
      }
      if (application.getBuyerInn() == null) {
        application.setBuyerInn(result.buyerInn());
      }
      if (application.getSellerInn() == null) {
        application.setSellerInn(result.sellerInn());
      }

      applicationRepository.save(application);

      file.setChecked(true);
      uploadedFileRepository.save(file);

    }

  }

  @Value("${integration.python.host}")
  private String pythonUrl;

  @SneakyThrows
  private ProcessResponse process(CustomMultipartFile multipartFile) {

// Обёртка над byte[] с указанием имени файла
    ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
      @Override
      public String getFilename() {
        return multipartFile.getOriginalFilename();
      }
    };

    HttpHeaders filePartHeaders = new HttpHeaders();
    filePartHeaders.setContentType(MediaType.APPLICATION_PDF);
    HttpEntity<Resource> fileEntity = new HttpEntity<>(fileResource, filePartHeaders);

// Собираем multipart body
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", fileEntity);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

    ResponseEntity<ProcessResponse> response = restTemplate.postForEntity(
      pythonUrl + "/process/",
      request,
      ProcessResponse.class
    );

    return response.getBody();
  }

}
