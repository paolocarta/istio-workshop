package com.example.guestbook;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GuestbookService {

  private static final Log log = LogFactory.getLog(GuestbookService.class);
  private final RestTemplate restTemplate;
  private final String endpoint;
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  public GuestbookService(RestTemplate restTemplate, String endpoint) {
    this.restTemplate = restTemplate;
    this.endpoint = endpoint;
  }

  public Map<String, String> add(String username, String message) {

    Map<String, String> payload = new HashMap<>();

    payload.put("username", username);
    payload.put("message", message);
    payload.put("timestamp", dateFormat.format(new Date()));

    return restTemplate.postForObject(endpoint, payload, Map.class);
  }

  public List<Map> allFallback() {
    Map<String, String> bulkheadEntry = new HashMap<>();
    bulkheadEntry.put("username", "system");
    bulkheadEntry.put("message", "Guestbook Service is currently unavailable");
    return Arrays.asList(bulkheadEntry);
  }

  public List<Map> all() {
    try {
      Map response = restTemplate.getForObject(endpoint, Map.class);

      Map embedded = (Map) response.get("_embedded");
      List<Map> messages = (List<Map>) embedded.get("messages");
      return messages.stream()
              .filter(message -> message.containsKey("_links"))
              .map(message -> (Map) message.get("_links"))
              .filter(links -> links.containsKey("self"))
              .map(links -> (Map) links.get("self"))
              .map(self -> (String) self.get("href"))
              .map(href -> restTemplate.getForObject(href, Map.class))
              .collect(Collectors.toList());
    } catch (HttpStatusCodeException e) {
      // Istio would've performed circuit breaking and retries
      // but it doesn't handle bulkheads / returning default values on full failures.
      log.error("Error from Guestbook Service, falling back", e);
      return allFallback();
    }
  }
}
