package kz.berekebank.bereke_deepmind.controller.models;

import java.util.List;

public record UploadFilesResponse(
  List<UploadFileResponse> ids
) {
}
