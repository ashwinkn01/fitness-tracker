package com.ashwin.financetracker.finance_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ashwin.financetracker.finance_tracker_api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
