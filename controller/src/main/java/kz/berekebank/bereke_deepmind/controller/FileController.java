package kz.berekebank.bereke_deepmind.controller;

import kz.berekebank.bereke_deepmind.controller.models.UploadFilesResponse;
import kz.berekebank.bereke_deepmind.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

  private final FileService fileService;

  @PostMapping("/upload")
  public UploadFilesResponse upload(@RequestParam("files") List<MultipartFile> files) {
    return fileService.upload(files);
  }

}
