package kz.berekebank.bereke_deepmind.feign;

import kz.berekebank.bereke_deepmind.feign.config.FeignMultipartFileConfig;
import kz.berekebank.bereke_deepmind.feign.models.python_service.OwnerResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "PythonClient", configuration = FeignMultipartFileConfig.class, url = "${integration.python.host}")
public interface PythonClient {

  @GetMapping("/egrul/")
  List<OwnerResponse> findOwners(@RequestParam("bin") String iin);

  @PostMapping(value = "/process/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ProcessResponse process(@RequestPart("file") MultipartFile file);

}
