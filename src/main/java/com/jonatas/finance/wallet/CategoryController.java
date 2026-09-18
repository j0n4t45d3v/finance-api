package com.jonatas.finance.wallet;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.common.dto.Response;
import com.jonatas.finance.infra.swagger.annotation.CategoryTag;
import com.jonatas.finance.infra.swagger.annotation.DefaultErrorResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@CategoryTag
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(operationId = "create", summary = "Cadastrar categoria")
    @DefaultErrorResponses
    @ApiResponse(responseCode = "201", description = "Created", headers = { @Header(name = "Location") })
    public ResponseEntity<?> create(@RequestBody @Valid CreateCategoryRequest request,
                                    @AuthenticationPrincipal User user) {
        var result = this.categoryService.create(request.toEntity(user));
        if (result.isFailure()) {
            var errorCode = result.getError();
            return ResponseEntity
                                 .status(HttpStatus.CONFLICT)
                                 .body(Response.ofError(errorCode, Response.Status.CONFLICT));
        }
        var categoryCreated = result.get();
        URI location = UriComponentsBuilder.fromPath("/categories/{id}")
                                           .buildAndExpand(categoryCreated.getId())
                                           .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    @Operation(operationId = "allCategories", summary = "Lista categorias")
    public ResponseEntity<Response<List<CategoryResponse>, Void>> all(@AuthenticationPrincipal User userAuthenticated) {
        List<CategoryResponse> categories = this.categoryService.findAllByUser(userAuthenticated)
                                                                .stream()
                                                                .map(CategoryResponse::of)
                                                                .toList();
        return ResponseEntity.ok(Response.of(categories));
    }
}
