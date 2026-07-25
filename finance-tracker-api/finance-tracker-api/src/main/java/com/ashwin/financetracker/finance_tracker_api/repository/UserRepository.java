package com.ashwin.financetracker.finance_tracker_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ashwin.financetracker.finance_tracker_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Used for Login: Returns an Optional to cleanly handle cases where the username isn't found
    Optional<User> findByUsername(String username);
    
    // Used for Signup: Returns true if the username is already in the database
    Boolean existsByUsername(String username);
    
    // Used for Signup: Returns true if the email is already in the database
    Boolean existsByEmail(String email);
}