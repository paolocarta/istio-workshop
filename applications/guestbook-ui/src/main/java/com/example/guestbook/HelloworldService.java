
package com.example.guestbook;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class HelloworldService {

  private final static Logger logger = LoggerFactory.getLogger(HelloworldService.class);

  private final RestTemplate restTemplate;
  private final String endpoint;

  public HelloworldService(RestTemplate restTemplate, String endpoint) {
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
  }

  public Map<String, String> greetingFallback() {

    Map<String, String> response = new HashMap<>();
    response.put("greeting", "Hello guest!");
    return response;
  }

  @HystrixCommand(fallbackMethod = "greetingFallback")
  public Map<String, String> greeting(String name) {

      return restTemplate.getForObject(endpoint + "/" + name, Map.class);

  }
}
