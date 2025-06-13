package kz.berekebank.bereke_deepmind.feign.config;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

public class FeignMultipartFileConfig {

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  @Bean
  public Encoder feignFormEncoder() {
    return new SpringFormEncoder(new SpringEncoder(messageConverters));
  }
}
