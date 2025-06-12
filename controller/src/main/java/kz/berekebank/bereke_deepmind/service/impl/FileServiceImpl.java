package kz.berekebank.bereke_deepmind.service.impl;

import kz.berekebank.bereke_deepmind.controller.models.UploadFileResponse;
import kz.berekebank.bereke_deepmind.controller.models.UploadFilesResponse;
import kz.berekebank.bereke_deepmind.repository.UploadedFileRepository;
import kz.berekebank.bereke_deepmind.repository.models.UploadedFile;
import kz.berekebank.bereke_deepmind.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class FileServiceImpl implements FileService {

  private final UploadedFileRepository uploadedFileRepository;

  @Override
  @SneakyThrows
  public UploadFilesResponse upload(List<MultipartFile> files) {

    List<UploadFileResponse> uploadFileResponses = new ArrayList<>();

    for (final MultipartFile file : files) {
      final UploadedFile uploadedFile = uploadedFileRepository.save(
        new UploadedFile()
          .withFilename(file.getOriginalFilename())
          .withContentType(file.getContentType())
          .withData(file.getBytes())
      );

      uploadFileResponses.add(new UploadFileResponse(uploadedFile.getId(),
                                                     uploadedFile.getFilename(),
                                                     uploadedFile.getContentType()));
    }

    return new UploadFilesResponse(uploadFileResponses);

  }

}
