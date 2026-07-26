package com.ashwin.financetracker.finance_tracker_api.service;

import com.ashwin.financetracker.finance_tracker_api.dto.CategoryDto;
import com.ashwin.financetracker.finance_tracker_api.entity.Category;
import com.ashwin.financetracker.finance_tracker_api.entity.User;
import com.ashwin.financetracker.finance_tracker_api.repository.CategoryRepository;
import com.ashwin.financetracker.finance_tracker_api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // --- SECURITY HELPER METHOD ---
    // This strictly enforces Data Isolation across all your services
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
    }

    // 1. Create a Category
    public Category createCategory(CategoryDto categoryDto) {
        User user = getAuthenticatedUser(); // Securely get the logged-in user

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setUser(user); // Tie the category exclusively to this user

        return categoryRepository.save(category);
    }

    // 2. Get all Categories for the logged-in user
    public List<Category> getUserCategories() {
        User user = getAuthenticatedUser();
        return categoryRepository.findByUserId(user.getId()); // Only fetch THEIR categories
    }
}