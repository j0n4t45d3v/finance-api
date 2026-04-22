package com.jonatas.finance.controller;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.dto.dashboard.*;
import com.jonatas.finance.infra.swagger.annotation.DashboardTag;
import com.jonatas.finance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@DashboardTag
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Resumo de receita vs despesas")
    public ResponseEntity<Response<SummaryIncomeVsExpense, Void>> summary(
        @ParameterObject @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.getSummaryIncomeVsExpense(request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/ranks/categories")
    @Operation(summary = "Rank das categorias com maiores movimentações")
    public ResponseEntity<Response<List<RankCategoryResponse>, Void>> rankCategories(
        @RequestParam(name = "type", required = false) Category.Type type,
        @RequestParam(name = "top", defaultValue = "10") Integer top,
        @ParameterObject @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.rankCategory(type, top, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/ranks/transactions")
    @Operation(summary = "Rank das transações financeiras com maiores valores movimentados")
    public ResponseEntity<Response<List<RankTransactionResponse>, Void>> rankTransactions(
        @RequestParam(name = "top", defaultValue = "10") Integer topTransactions,
        @ParameterObject @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.rankTransactions(topTransactions, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    public enum RankCategoryGroupBy {
        CATEGORY, MONTH, DAY
    }

    @GetMapping("/transactions")
    @Operation(summary = "Trás as transações agrupadas por Categoria, mês ou dia")
    public ResponseEntity<Response<List<TransactionGroupByResponse>, Void>> transactions(
        @RequestParam(defaultValue = "CATEGORY") RankCategoryGroupBy rankCategoryGroupBy,
        @ParameterObject @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.transactions(rankCategoryGroupBy, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/transactions/lastest")
    @Operation(summary = "Últimas transações feitas")
    public ResponseEntity<Response<List<RankTransactionResponse>, Void>> lastTransactions(
        @RequestParam(name = "top", defaultValue = "10") Integer topTransactions,
        @ParameterObject @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.lastTransactions(topTransactions, request, user);
        return ResponseEntity.ok(Response.of(response));
    }
}
