package com.employee.analysis.employeeanalysis.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public List <User> findByUsernames (String username);    
    public User findByUsername (String username);

}
