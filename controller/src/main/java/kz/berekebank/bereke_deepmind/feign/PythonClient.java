package kz.berekebank.bereke_deepmind.feign;

import kz.berekebank.bereke_deepmind.feign.config.FeignMultipartFileConfig;
import kz.berekebank.bereke_deepmind.feign.models.python_service.OwnerResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessResponse;
import kz.berekebank.bereke_deepmind.feign.models.python_service.ProcessTextDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PythonClient", configuration = FeignMultipartFileConfig.class, url = "${integration.python.host}")
public interface PythonClient {

  @GetMapping("/egrul/")
  List<OwnerResponse> findOwners(@RequestParam("bin") String iin);

  @PostMapping(value = "/processText/")
  ProcessResponse process(@RequestBody ProcessTextDto processTextDto);

  @PostMapping(value = "/compliance/")
  String compliance(@RequestBody ProcessTextDto processTextDto);

}
