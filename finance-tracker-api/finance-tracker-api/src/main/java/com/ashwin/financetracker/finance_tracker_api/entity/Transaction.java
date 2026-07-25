package com.ashwin.financetracker.finance_tracker_api.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "transactions")
@Getter @Setter @NoArgsConstructor
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) private User user;
    @ManyToOne @JoinColumn(name = "category_id", nullable = false) private Category category;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Column(nullable = false) @Enumerated(EnumType.STRING) private TransactionType type;
    @Column(name = "txn_date", nullable = false) private java.time.LocalDate txnDate;
    @Column(name = "txn_time", nullable = false) private java.time.LocalTime txnTime;
    @Column(length = 255) private String note;
    @Column(name = "created_at", updatable = false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
