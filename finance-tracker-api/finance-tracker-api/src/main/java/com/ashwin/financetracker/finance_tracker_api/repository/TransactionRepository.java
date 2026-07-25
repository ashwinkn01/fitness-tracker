package com.ashwin.financetracker.finance_tracker_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ashwin.financetracker.finance_tracker_api.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdAndTxnDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT t.category.name, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
           "AND FUNCTION('DATE_FORMAT', t.txnDate, '%Y-%m') = :monthYear " +
           "GROUP BY t.category.name")
    List<Object[]> sumExpensesByCategory(@Param("userId") Long userId, @Param("monthYear") String monthYear);
}
