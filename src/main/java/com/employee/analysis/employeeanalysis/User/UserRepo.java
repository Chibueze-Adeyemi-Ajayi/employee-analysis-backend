package com.employee.analysis.employeeanalysis.User;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // public List <User> findByUsernames (String username);    
    public User findByUsername (String username);
}
