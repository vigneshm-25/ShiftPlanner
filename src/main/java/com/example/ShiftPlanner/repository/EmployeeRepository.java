package com.example.ShiftPlanner.repository;

import com.example.ShiftPlanner.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}