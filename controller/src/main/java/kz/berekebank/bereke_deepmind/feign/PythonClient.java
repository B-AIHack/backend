package kz.berekebank.bereke_deepmind.feign;

import kz.berekebank.bereke_deepmind.feign.config.FeignConfig;
import kz.berekebank.bereke_deepmind.feign.models.python_service.OwnerResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "PythonClient", configuration = FeignConfig.class, url = "${integration.python.host}")
public interface PythonClient {

  @GetMapping("/find-owners")
  List<OwnerResponse> findOwners(@RequestParam("iin") String iin);

  @PostMapping("/process")
  ProcessResponse process(@RequestPart("file") MultipartFile file);

}
