package com.jonatas.finance.controller;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Category.Name;
import com.jonatas.finance.domain.Category.Type;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.infra.swagger.annotation.CategoryTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;
import com.jonatas.finance.service.CategoryService;
import com.jonatas.finance.service.CreateService;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CategoryTag
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CreateService<Category> createService;
    private final CategoryService categoryService;

    public CategoryController(
        CreateService<Category> createService,
        CategoryService categoryService
    ) {
        this.createService = createService;
        this.categoryService = categoryService;
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
    @ApiResponse(responseCode = "201", description = "Created", headers = {@Header(name = "Location")})
    public ResponseEntity<Void> create(
        @RequestBody @Valid CreateCategoryRequest request,
        @AuthenticationPrincipal User user) {
        Category categoryCreated = this.createService.execute(request.toEntity(user));
        URI location = UriComponentsBuilder
            .fromPath("/categories/{id}")
            .buildAndExpand(categoryCreated.getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }

    public record CategoryResponse(Long id, String name, String type) {
    }

    @GetMapping
    public ResponseEntity<Response<List<CategoryResponse>, Void>> all(@AuthenticationPrincipal User userAuthenticated) {
        List<CategoryResponse> categories = this.categoryService
            .findAllByUser(userAuthenticated)
            .stream()
            .map(c -> new CategoryResponse(c.getId(), c.getNameValue(), c.getType().name()))
            .toList();
        return ResponseEntity.ok(Response.of(categories));
    }

}
