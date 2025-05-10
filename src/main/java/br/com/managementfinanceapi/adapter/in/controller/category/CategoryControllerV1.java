package br.com.managementfinanceapi.adapter.in.controller.category;

import br.com.managementfinanceapi.application.core.domain.category.Category;
import br.com.managementfinanceapi.application.core.domain.category.dto.CreateCategory;
import br.com.managementfinanceapi.application.core.domain.category.dto.FiltersTotalByCategory;
import br.com.managementfinanceapi.application.core.domain.category.dto.TotalByCategoryView;
import br.com.managementfinanceapi.application.port.in.category.CreateCategoryGateway;
import br.com.managementfinanceapi.application.port.in.category.FindAllCategoriesGateway;
import br.com.managementfinanceapi.application.port.in.category.SearchTotalByCategoryGateway;
import br.com.managementfinanceapi.infra.http.dto.ResponseV0;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryControllerV1 {
  private final CreateCategoryGateway createCategoryGateway;
  private final FindAllCategoriesGateway findAllCategoriesGateway;
  private final SearchTotalByCategoryGateway searchTotalByCategoryGateway;

  public CategoryControllerV1(CreateCategoryGateway createCategoryGateway, FindAllCategoriesGateway findAllCategoriesGateway, SearchTotalByCategoryGateway searchTotalByCategoryGateway) {
    this.createCategoryGateway = createCategoryGateway;
    this.findAllCategoriesGateway = findAllCategoriesGateway;
    this.searchTotalByCategoryGateway = searchTotalByCategoryGateway;
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

  @GetMapping("/transactions")
  public ResponseEntity<ResponseV0<Page<TotalByCategoryView>>> getAllTransactionsGroupedInCategory(
      @Valid @ModelAttribute FiltersTotalByCategory filters,
      Pageable page
  ) {
    var result = this.searchTotalByCategoryGateway.execute(filters, page);
    return ResponseEntity.ok(ResponseV0.ok(result));
  }
}
