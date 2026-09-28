package com.example.ShiftPlanner.repository;

import com.example.ShiftPlanner.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
}