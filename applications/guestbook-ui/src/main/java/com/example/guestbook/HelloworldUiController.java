
package com.example.guestbook;

import java.util.Map;

import org.infinispan.client.hotrod.RemoteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
//@SessionAttributes("name")
public class HelloworldUiController {

  private final static Logger logger = LoggerFactory.getLogger(HelloworldUiController.class);

  @Autowired
  private RemoteCache<String, String> defaultCache;

  private final HelloworldService helloworldService;
  private final GuestbookService guestbookService;

  public HelloworldUiController(
      HelloworldService helloworldService, 
      GuestbookService guestbookService) {
        
    this.helloworldService = helloworldService;
    this.guestbookService = guestbookService;
  }

  @GetMapping("/")
  public String index(Model model) {

    logger.info("Inside get index method");

//    defaultCache.getName();

    if (model.containsAttribute("name")) {
      String name = (String) model.asMap().get("name");
      Map<String, String> greeting = helloworldService.greeting(name);
      model.addAttribute("greeting", greeting);
    }

    model.addAttribute("messages", guestbookService.all());

    return "index";
  }

  @PostMapping("/greet")
  public String greet(
      @RequestParam String name, 
      @RequestParam String message, Model model) {

    model.addAttribute("name", name);
    if (message != null && !message.trim().isEmpty()) {
      
      guestbookService.add(name, message);
    }

    return "redirect:/";
  }
}
