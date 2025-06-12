package kz.berekebank.bereke_deepmind.service;

import kz.berekebank.bereke_deepmind.controller.models.UploadFilesResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

  UploadFilesResponse upload(List<MultipartFile> files);

}
