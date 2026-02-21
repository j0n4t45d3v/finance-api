package com.jonatas.finance.controller;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.Response;
import com.jonatas.finance.dto.dashboard.*;
import com.jonatas.finance.infra.swagger.annotation.DashboardTag;
import com.jonatas.finance.service.DashboardService;
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
    public ResponseEntity<Response<SummaryIncomeVsExpense, Void>> summary(
        @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.getSummaryIncomeVsExpense(request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/ranks/categories")
    public ResponseEntity<Response<List<RankCategoryResponse>, Void>> rankCategories(
        @RequestParam(name= "type", required = false) Category.Type type,
        @RequestParam(name= "top", defaultValue = "10") Integer top,
        @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.rankCategory(type, top, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/ranks/transactions")
    public ResponseEntity<Response<List<RankTransactionResponse>, Void>> rankTransactions(
        @RequestParam(name= "top", defaultValue = "10") Integer topTransactions,
        @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.rankTransactions(topTransactions, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    public enum RankCategoryGroupBy {
        CATEGORY, MONTH, DAY;
    }

    @GetMapping("/transactions")
    public ResponseEntity<Response<List<TransactionGroupByResponse>, Void>> transactions(
        @RequestParam(defaultValue = "CATEGORY") RankCategoryGroupBy rankCategoryGroupBy,
        @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.transactions(rankCategoryGroupBy, request, user);
        return ResponseEntity.ok(Response.of(response));
    }

    @GetMapping("/transactions/lastest")
    public ResponseEntity<Response<List<RankTransactionResponse>, Void>> lastTransactions(
        @RequestParam(name= "top", defaultValue = "10") Integer topTransactions,
        @ModelAttribute DashboardFiltersRequest request,
        @AuthenticationPrincipal User user
    ) {
        var response = this.dashboardService.lastTransactions(topTransactions, request, user);
        return ResponseEntity.ok(Response.of(response));
    }
}
