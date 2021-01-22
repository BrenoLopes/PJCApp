package br.balladesh.pjcappbackend.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class UploadConfiguration {
  @Bean
  public MultipartConfigElement multipartConfigElement() {
    long fSize = 1024 * 1024 * 20;

    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofBytes(fSize));
    factory.setMaxRequestSize(DataSize.ofBytes(fSize));
    return factory.createMultipartConfig();

//    CommonsMultipartResolver multipartResolver
//        = new CommonsMultipartResolver();
//    multipartResolver.setMaxUploadSize(1024 * 1024 * 20);
//    return multipartResolver;
  }
}
