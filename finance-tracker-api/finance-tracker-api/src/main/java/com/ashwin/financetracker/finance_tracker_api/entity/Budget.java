package com.ashwin.financetracker.finance_tracker_api.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "budgets", uniqueConstraints = {
    @UniqueConstraint(name = "uq_budget", columnNames = {"user_id", "category_id", "month_year"})
})
@Getter @Setter @NoArgsConstructor
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false) private User user;
    @ManyToOne @JoinColumn(name = "category_id", nullable = false) private Category category;
    @Column(nullable = false) private String monthYear; // Format: YYYY-MM
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal limitAmount;
}
