package com.jonatas.finance.repository;

import com.jonatas.finance.domain.Category;
import com.jonatas.finance.domain.Transaction;
import com.jonatas.finance.domain.User;
import com.jonatas.finance.dto.dashboard.RankCategoryResponse;
import com.jonatas.finance.dto.dashboard.RankTransactionResponse;
import com.jonatas.finance.dto.dashboard.SummaryIncomeVsExpense;
import com.jonatas.finance.dto.dashboard.TransactionGroupByResponse;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DashboardRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT new com.jonatas.finance.dto.dashboard.SummaryIncomeVsExpense(
            SUM(CASE
                    WHEN c.type = com.jonatas.finance.domain.Category.Type.INCOME THEN t.amount.value
                    ELSE 0
                END),
            SUM(CASE
                    WHEN c.type = com.jonatas.finance.domain.Category.Type.EXPENSE THEN t.amount.value
                    ELSE 0
                END),
            SUM(t.amount.value * CASE
                                     WHEN category.type = com.jonatas.finance.domain.Category.Type.EXPENSE THEN -1
                                     ELSE 1
                                 END )
        )
        FROM Transaction t
        INNER JOIN Category c
            ON c.id = t.category.id
        WHERE t.user = :user
          AND t.transactionAt.value BETWEEN :startDate AND :endDate
        """)
    SummaryIncomeVsExpense findSummaryIncomesVsExpenses(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate
    );

    @Query("""
        select new com.jonatas.finance.dto.dashboard.RankCategoryResponse(
                   c.name.value,
                   concat(c.type, ''),
                   sum(t.amount.value)
               )
         from Category c
         inner join Transaction t
             on t.category.id = c.id
         where t.user = ?1
           and t.transactionAt.value between ?2 and ?3
           and (?4 is null or c.type = ?4)
         group by c.name, c.type
         order by sum(t.amount.value) desc
        """)
    List<RankCategoryResponse> findTopRankCategory(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate,
        @Nonnull Category.Type type,
        Pageable pageable
    );


    @Query("""
        select new com.jonatas.finance.dto.dashboard.RankTransactionResponse(
                   concat(c.type, ''),
                   c.name.value,
                   t.amount.value,
                   t.transactionAt.value
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
        """)
    List<RankTransactionResponse> findTopRankTransaction(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate,
        Pageable pageable
    );

    @Query("""
        select new com.jonatas.finance.dto.dashboard.TransactionGroupByResponse(
                   c.name.value,
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
        group by c.name.value
        """)
    List<TransactionGroupByResponse> findTransactionGroupByCategory(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate
    );

    @Query("""
        select new com.jonatas.finance.dto.dashboard.TransactionGroupByResponse(
                   YEAR(t.transactionAt.value),
                   MONTH(t.transactionAt.value),
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
        group by YEAR(t.transactionAt.value), MONTH(t.transactionAt.value)
        """)
    List<TransactionGroupByResponse> findTransactionGroupByMonth(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate
    );

    @Query("""
        select new com.jonatas.finance.dto.dashboard.TransactionGroupByResponse(
                   concat(cast(t.transactionAt.value as date), ''),
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = com.jonatas.finance.domain.Category.Type.EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
        group by cast(t.transactionAt.value as date)
        """)
    List<TransactionGroupByResponse> findTransactionGroupByDay(
        @Nonnull User user,
        @Nonnull LocalDateTime startDate,
        @Nonnull LocalDateTime endDate
    );
}
