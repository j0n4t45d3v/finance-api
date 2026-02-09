package com.jonatas.finance.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Category.Name;
import com.jonatas.finance.domain.Category.Type;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.CreateService;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

  private final CreateService<Category> createService;

  public CategoryController(CreateService<Category> createService) {
    this.createService = createService;
  }

  public record CreateCategoryRequest(
      @NotBlank(message = "name is required")
      @Size(min = 5, max = 50, message = "name must be between 5 and 50 characters") 
      String name,

      @NotNull(message = "type is required") 
      Type type
  ) {
    private Category toEntity(User user) {
      return new Category(new Name(this.name), this.type, user);
    }
  }

  @PostMapping
  @DefaultErrorResponses
  @ApiResponse(responseCode = "201", description = "Created", headers = { @Header(name = "Location") })
  public ResponseEntity<Void> create(
        @RequestBody @Valid CreateCategoryRequest request,
        @AuthenticationPrincipal User user
    ) {
    Category categoryCreated = this.createService.execute(request.toEntity(user));
    URI location = UriComponentsBuilder
        .fromPath("/categories/{id}")
        .buildAndExpand(categoryCreated.getId())
        .toUri();
    return ResponseEntity.created(location).build();
  }

}
