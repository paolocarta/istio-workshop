package com.example.guestbook;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.SaslQop;
import org.infinispan.spring.remote.provider.SpringRemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Configuration
//@EnableInfinispanRemoteHttpSession
@EnableCaching
public class ApplicationConfig {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

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

//    remoteCacheManager.administration().getOrCreateCache("sessions", "default");
    SpringRemoteCacheManager cacheManager = new SpringRemoteCacheManager(remoteCacheManager);

    return cacheManager;
  }

  @Bean
  @Qualifier("defaultCache")
  public RemoteCache<String, String> defaultCache(RemoteCacheManager cacheManager) {
    Set<String> cacheNames = cacheManager.getCacheNames();

    logger.info("cache names: " + cacheNames.toString());

    return null;

  }

  @Value("infinispan.application.name")
  private String appName;

  @Value("infinispan.remote.auth-username")
  private String username;

  @Value("infinispan.remote.auth-password")
  private String password;

  @Bean
  public org.infinispan.client.hotrod.configuration.Configuration configuration(){

    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServer()
            //Connection
            .host(appName)
            .port(11222)
            //Client intelligence
            .clientIntelligence(ClientIntelligence.BASIC)
            .security()
            //Authentication
            .authentication().enable()
            .username(username)
            .password(password)
            .serverName(appName)
            .saslQop(SaslQop.AUTH)
            .saslMechanism("DIGEST-MD5")
            //Encryption
            .ssl()
            .sniHostName(appName)
            .trustStorePath("/var/run/secrets/kubernetes.io/serviceaccount/service-ca.crt");

    return builder.build();
  }


}
