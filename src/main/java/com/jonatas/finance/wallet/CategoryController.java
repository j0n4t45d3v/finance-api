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
import com.jonatas.finance.wallet.Category.Name;
import com.jonatas.finance.wallet.Category.Type;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@CategoryTag
@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Schema(description = "Requisição de criação de uma categoria")
    public record CreateCategoryRequest(
                                        @Schema(example = "Alimentação") @NotBlank(message = "name is required") @Size(min = 5, max = 50, message = "name must be between 5 and 50 characters") String name,
                                        @Schema(example = "EXPENSE") @NotNull(message = "type is required") Type type) {

        private Category toEntity(User user) {
            return new Category(Name.of(this.name), this.type, user);
        }
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
                                 .body(Response.ofError(errorCode.message(), Response.Status.CONFLICT));
        }
        var categoryCreated = result.get();
        URI location = UriComponentsBuilder.fromPath("/categories/{id}")
                                           .buildAndExpand(categoryCreated.getId())
                                           .toUri();
        return ResponseEntity.created(location).build();
    }

    @Schema(description = "Categoria")
    public record CategoryResponse(@Schema(example = "1") Long id,
                                   @Schema(example = "Alimentação") String name,
                                   @Schema(example = "EXPENSE") String type) {

        public static CategoryResponse of(Category category) {
            return new CategoryResponse(category.getId(), category.getNameValue(), category.getType().name());
        }
    }

    @GetMapping
    @Operation(operationId = "allCategories", summary = "Lista categorias")
    public ResponseEntity<Response<List<CategoryResponse>, Void>> all(
                                                                      @AuthenticationPrincipal User userAuthenticated) {
        List<CategoryResponse> categories = this.categoryService.findAllByUser(userAuthenticated)
                                                                .stream()
                                                                .map(CategoryResponse::of)
                                                                .toList();
        return ResponseEntity.ok(Response.of(categories));
    }
}
