package br.com.managementfinanceapi.category.controller;

import br.com.managementfinanceapi.category.domain.Category;
import br.com.managementfinanceapi.category.domain.dto.CreateCategory;
import br.com.managementfinanceapi.category.gateway.CreateCategoryGateway;
import br.com.managementfinanceapi.category.gateway.FindAllCategoriesGateway;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryControllerV1 {
  private final CreateCategoryGateway createCategoryGateway;
  private final FindAllCategoriesGateway findAllCategoriesGateway;

  public CategoryControllerV1(CreateCategoryGateway createCategoryGateway, FindAllCategoriesGateway findAllCategoriesGateway) {
    this.createCategoryGateway = createCategoryGateway;
    this.findAllCategoriesGateway = findAllCategoriesGateway;
  }

  @PostMapping
  public ResponseEntity<ResponseV0<Category>> create(@Valid @RequestBody CreateCategory category) {
    var categoryCreated = this.createCategoryGateway.execute(category);
    ResponseV0<Category> response = ResponseV0.created("Category created successfully", categoryCreated);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping
  public ResponseEntity<ResponseV0<List<Category>>> getAllByRecipe() {
    var categoryCreated = this.findAllCategoriesGateway.execute();
    return ResponseEntity.ok(ResponseV0.ok(categoryCreated));
  }
}
