package com.jonatas.finance.analytic;

import com.jonatas.finance.auth.User;
import com.jonatas.finance.wallet.Category;
import com.jonatas.finance.wallet.Transaction;
import jakarta.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DashboardRepository extends JpaRepository<Transaction, Long> {

  @Query(
      """
        SELECT new com.jonatas.finance.analytic.SummaryIncomeVsExpense(
            SUM(CASE
                    WHEN c.type = INCOME THEN t.amount.value
                    ELSE 0
                END),
            SUM(CASE
                    WHEN c.type = EXPENSE THEN t.amount.value
                    ELSE 0
                END),
            SUM(t.amount.value * CASE
                                     WHEN category.type = EXPENSE THEN -1
                                     ELSE 1
                                 END )
        )
        FROM Transaction t
        INNER JOIN Category c
            ON c.id = t.category.id
        WHERE t.user = :user
          AND t.transactionAt.value BETWEEN :startDate AND :endDate
          AND (:walletId is null or t.wallet.id = :walletId)
        """)
  SummaryIncomeVsExpense findSummaryIncomesVsExpenses(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Long walletId);

  @Query(
      """
        select new com.jonatas.finance.analytic.RankCategoryResponse(
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
           and (?5 is null or t.wallet.id = ?5)
         group by c.name, c.type
         order by sum(t.amount.value) desc
        """)
  List<RankCategoryResponse> findTopRankCategory(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Category.Type type,
      Long walletId,
      Pageable pageable);

  @Query(
      """
        select new com.jonatas.finance.analytic.RankTransactionResponse(
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
          and (?4 is null or t.wallet.id = ?4)
        """)
  List<RankTransactionResponse> findTopRankTransaction(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Long walletId,
      Pageable pageable);

  @Query(
      """
        select new com.jonatas.finance.analytic.TransactionGroupByResponse(
                   c.name.value,
                   SUM(CASE
                            WHEN c.type = INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
          and (?4 is null or t.wallet.id = ?4)
        group by c.name.value
        """)
  List<TransactionGroupByResponse> findTransactionGroupByCategory(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Long walletId);

  @Query(
      """
        select new com.jonatas.finance.analytic.TransactionGroupByResponse(
                   YEAR(t.transactionAt.value),
                   MONTH(t.transactionAt.value),
                   SUM(CASE
                            WHEN c.type = INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
          and (?4 is null or t.wallet.id = ?4)
        group by YEAR(t.transactionAt.value), MONTH(t.transactionAt.value)
        """)
  List<TransactionGroupByResponse> findTransactionGroupByMonth(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Long walletId);

  @Query(
      """
        select new com.jonatas.finance.analytic.TransactionGroupByResponse(
                   concat(cast(t.transactionAt.value as date), ''),
                   SUM(CASE
                            WHEN c.type = INCOME THEN t.amount.value
                            ELSE 0
                       END),
                   SUM(CASE
                            WHEN c.type = EXPENSE THEN t.amount.value
                            ELSE 0
                       END),
                   COUNT(*)
               )
        from Transaction t
        inner join Category c
         on t.category.id = c.id
        where t.user = ?1
          and t.transactionAt.value between ?2 and ?3
          and (?4 is null or t.wallet.id = ?4)
        group by cast(t.transactionAt.value as date)
        """)
  List<TransactionGroupByResponse> findTransactionGroupByDay(
      @Nonnull User user,
      @Nonnull LocalDateTime startDate,
      @Nonnull LocalDateTime endDate,
      Long walletId);
}
