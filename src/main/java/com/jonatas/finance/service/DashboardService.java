package com.jonatas.finance.service;

import com.jonatas.finance.controller.DashboardController;
import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.dashboard.*;

import java.util.List;

public interface DashboardService {

    SummaryIncomeVsExpense getSummaryIncomeVsExpense(DashboardFiltersRequest filters, User user);

    List<RankCategoryResponse> rankCategory(Category.Type type, Integer topIncomes, DashboardFiltersRequest request, User user);

    List<RankTransactionResponse> rankTransactions(
        Integer topTransactions,
        DashboardFiltersRequest request,
        User user
    );


    List<TransactionGroupByResponse> transactions(
        DashboardController.DashboardTransactionGroupBy dashboardTransactionGroupBy,
        DashboardFiltersRequest request,
        User user
    );

    List<RankTransactionResponse> lastTransactions(
        Integer topTransactions,
        DashboardFiltersRequest request,
        User user
    );

}
