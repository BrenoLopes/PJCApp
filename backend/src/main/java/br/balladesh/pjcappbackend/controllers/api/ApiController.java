package br.balladesh.pjcappbackend.controllers.api;

import br.balladesh.pjcappbackend.dto.api.ApiMessage;
import br.balladesh.pjcappbackend.dto.api.Endpoint;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ApiController {

  @GetMapping
  public ResponseEntity<?> home(HttpServletRequest request) {
    ImmutableMap<String, Endpoint> securityEndpoints = ImmutableMap.of(
        "login", new Endpoint("/api/auth/login", "post"),
        "refresh", new Endpoint("/api/auth/refresh", "get"),
        "signup", new Endpoint("/api/auth/signup", "post")
    );

    ImmutableMap<String, Endpoint> artistsEndpoints = ImmutableMap.of(
        "filter", new Endpoint("/api/artists", "get"),
        "add", new Endpoint("/api/artists", "post"),
        "edit", new Endpoint("/api/artists", "put"),
        "remove", new Endpoint("/api/artists", "delete"),
        "list", new Endpoint("/api/artists/list", "get")
    );

    ImmutableMap<String, Endpoint> albumEndpoints = ImmutableMap.of(
        "filter", new Endpoint("/api/albums", "get"),
        "add", new Endpoint("/api/albums", "post"),
        "edit", new Endpoint("/api/albums", "put"),
        "remove", new Endpoint("/api/albums", "delete"),
        "list", new Endpoint("/api/albums/list", "get")
    );

    ApiMessage message = new ApiMessage(
        "Welcome to the api. Check out the url for each endpoint.",
        securityEndpoints,
        artistsEndpoints,
        albumEndpoints
    );

    return ResponseEntity.ok(message);
  }
}
