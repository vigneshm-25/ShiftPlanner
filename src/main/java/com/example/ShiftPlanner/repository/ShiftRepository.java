package com.example.ShiftPlanner.repository;

import com.example.ShiftPlanner.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}