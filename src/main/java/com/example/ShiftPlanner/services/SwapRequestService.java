package com.example.ShiftPlanner.services;

import com.example.ShiftPlanner.model.SwapRequest;

import java.util.List;

public interface SwapRequestService {

    SwapRequest createSwapRequest(SwapRequest swapRequest);

    List<SwapRequest> getAllSwapRequests();

    SwapRequest getSwapRequestById(Long id);

    SwapRequest updateSwapRequest(Long id, SwapRequest swapRequest);

    void deleteSwapRequest(Long id);
}