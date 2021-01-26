package br.balladesh.pjcappbackend.controllers.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  @RequestMapping(value = "/", produces = "text/html")
  public String home() {
    return "index";
  }

  @RequestMapping(value = "/**/{path:[^.]*}")
  public String redirect() {
    return "forward:/";
  }
}
