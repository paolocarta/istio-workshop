package com.example.guestbook;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.spring.remote.provider.SpringRemoteCacheManager;
import org.infinispan.spring.remote.session.configuration.EnableInfinispanRemoteHttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableInfinispanRemoteHttpSession
@EnableCaching
public class ApplicationConfig {

  @Bean
  RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }

  @Bean
  HelloworldService helloworldService(RestTemplate restTemplate, 
                          @Value("${backend.helloworld-service.url}") String endpoint) {
    return new HelloworldService(restTemplate, endpoint);
  }

  @Bean
  GuestbookService guestbookService(RestTemplate restTemplate, 
                          @Value("${backend.guestbook-service.url}") String endpoint) {
    return new GuestbookService(restTemplate, endpoint);
  }

  @Bean
  public SpringRemoteCacheManager springRemoteCacheManager(RemoteCacheManager remoteCacheManager) {

    remoteCacheManager.administration().getOrCreateCache("sessions", "default");
    SpringRemoteCacheManager cacheManager = new SpringRemoteCacheManager(remoteCacheManager);

    return cacheManager;
  }
}
