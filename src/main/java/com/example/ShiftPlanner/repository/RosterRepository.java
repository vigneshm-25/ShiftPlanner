package com.example.ShiftPlanner.repository;

import com.example.ShiftPlanner.model.Roster;
import com.example.ShiftPlanner.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RosterRepository extends JpaRepository<Roster, Long> {

    List<Roster> findByEmployee(Employee employee);
}