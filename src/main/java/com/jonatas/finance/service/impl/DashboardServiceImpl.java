package com.jonatas.finance.service.impl;

import com.jonatas.finance.controller.DashboardController;
import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.dashboard.*;
import com.jonatas.finance.repository.DashboardRepository;
import com.jonatas.finance.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public SummaryIncomeVsExpense getSummaryIncomeVsExpense(DashboardFiltersRequest filters, User user) {
        return this.dashboardRepository
            .findSummaryIncomesVsExpenses(user, filters.getStartTimestamp(), filters.getEndTimestamp(), filters.accountId());
    }

    @Override
    public List<RankCategoryResponse> rankCategory(Category.Type type, Integer top, DashboardFiltersRequest request, User user) {
        Pageable limit = PageRequest.of(0, top);
        return this.dashboardRepository.findTopRankCategory(
            user,
            request.getStartTimestamp(),
            request.getEndTimestamp(),
            type,
            request.accountId(),
            limit
        );
    }

    @Override
    public List<RankTransactionResponse> rankTransactions(Integer topTransactions, DashboardFiltersRequest request, User user) {
        Pageable limit = PageRequest.of(0, topTransactions, Sort.by(Sort.Direction.DESC, "amount"));
        return this.dashboardRepository.findTopRankTransaction(
            user,
            request.getStartTimestamp(),
            request.getEndTimestamp(),
            request.accountId(),
            limit
        );
    }

    @Override
    public List<TransactionGroupByResponse> transactions(DashboardController.RankCategoryGroupBy rankCategoryGroupBy, DashboardFiltersRequest request, User user) {
        return switch (rankCategoryGroupBy) {
            case DAY -> this.dashboardRepository.findTransactionGroupByDay(
                user,
                request.getStartTimestamp(),
                request.getEndTimestamp(),
                request.accountId()
            );

            case MONTH -> this.dashboardRepository.findTransactionGroupByMonth(
                user,
                    request.getStartTimestamp(),
                    request.getEndTimestamp(),
                    request.accountId()
            );
            default -> this.dashboardRepository.findTransactionGroupByCategory(
                user,
                request.getStartTimestamp(),
                request.getEndTimestamp(),
                request.accountId()
            );
        };
    }

    @Override
    public List<RankTransactionResponse> lastTransactions(Integer top, DashboardFiltersRequest request, User user) {
        Pageable limit = PageRequest.of(0, top, Sort.by(Sort.Direction.DESC, "transactionAt"));
        return this.dashboardRepository.findTopRankTransaction(
            user,
            request.getStartTimestamp(),
            request.getEndTimestamp(),
            request.accountId(),
            limit
        );
    }

}
