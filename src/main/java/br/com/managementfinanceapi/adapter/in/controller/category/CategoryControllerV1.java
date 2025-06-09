package br.com.managementfinanceapi.adapter.in.controller.category;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.managementfinanceapi.adapter.in.dto.ResponseV0;
import br.com.managementfinanceapi.adapter.in.dto.category.CreateCategory;
import br.com.managementfinanceapi.application.core.domain.category.CategoryDomain;
import br.com.managementfinanceapi.application.port.in.category.CreateCategoryPort;
import br.com.managementfinanceapi.application.port.in.category.SearchCategoryPort;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/categories")
public class CategoryControllerV1 {

  private final CreateCategoryPort createCategoryGateway;
  private final SearchCategoryPort searchCategoryPort;

  public CategoryControllerV1(
    CreateCategoryPort createCategoryGateway,
    SearchCategoryPort searchCategoryPort
  ) {
    this.createCategoryGateway = createCategoryGateway;
    this.searchCategoryPort = searchCategoryPort;
  }

  @PostMapping
  public ResponseEntity<ResponseV0<?>> create(@Valid @RequestBody CreateCategory category) {
    CategoryDomain categoryCreated = this.createCategoryGateway.execute(category.toDomain());
    ResponseV0<String> response = ResponseV0.created("Category created successfully");
    URI uri = UriComponentsBuilder
                .fromPath("/{userId}/{id}")
                .build(categoryCreated.getUser().getId(), categoryCreated.getId());
    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ResponseV0<List<CategoryDomain>>> getAll(@PathVariable("userId") Long userId) {
    var categoryCreated = this.searchCategoryPort.all();
    return ResponseEntity.ok(ResponseV0.ok(categoryCreated));
  }

}