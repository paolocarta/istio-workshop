package com.example.guestbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController
class HelloworldController {

  private static final Logger log = LoggerFactory.getLogger(HelloworldController.class);

  @Value("${version:1.0}")
  public String version;

  @GetMapping("/hello/{name}")
  public Map<String, String> hello(
          @Value("${greeting}") String greetingTemplate,
          @PathVariable String name) throws UnknownHostException {

  	log.info("Recevied hello request for: " + name);

    Map<String, String> response = new HashMap<>();

    String hostname = InetAddress.getLocalHost().getHostName();

    String greeting = greetingTemplate
        .replaceAll("\\$name", name)
        .replaceAll("\\$hostname", hostname)
        .replaceAll("\\$version", version);

    response.put("greeting", greeting);
    response.put("version", version);
    response.put("hostname", hostname);

    return response;
  }
}
