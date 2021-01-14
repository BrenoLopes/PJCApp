package br.balladesh.pjcappbackend.controllers.api;

import br.balladesh.pjcappbackend.dto.api.ApiMessage;
import br.balladesh.pjcappbackend.dto.api.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

  @GetMapping
  public ResponseEntity<ApiMessage> home(HttpServletRequest request) {
    ApiMessage message = new ApiMessage(
        "Welcome to the api. Check out the url for each endpoint.",
        new Endpoint(String.format("%s/auth/login", request.getRequestURL()), "POST"),
        new Endpoint(String.format("%s/auth/signup", request.getRequestURL()), "POST"),
        new Endpoint(String.format("%s/auth/refresh", request.getRequestURL()), "GET")
    );

    return ResponseEntity.ok(message);
  }
}
