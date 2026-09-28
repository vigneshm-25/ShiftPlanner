package com.example.ShiftPlanner.controller;

import com.example.ShiftPlanner.model.SwapRequest;
import com.example.ShiftPlanner.services.SwapRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swap-requests")
public class SwapRequestController {

    private final SwapRequestService swapRequestService;

    public SwapRequestController(SwapRequestService swapRequestService) {
        this.swapRequestService = swapRequestService;
    }

    @PostMapping
    public SwapRequest createSwapRequest(
            @RequestBody SwapRequest swapRequest) {

        return swapRequestService.createSwapRequest(swapRequest);
    }

    @GetMapping
    public List<SwapRequest> getAllSwapRequests() {
        return swapRequestService.getAllSwapRequests();
    }

    @GetMapping("/{id}")
    public SwapRequest getSwapRequestById(
            @PathVariable Long id) {

        return swapRequestService.getSwapRequestById(id);
    }

    @PutMapping("/{id}")
    public SwapRequest updateSwapRequest(
            @PathVariable Long id,
            @RequestBody SwapRequest swapRequest) {

        return swapRequestService.updateSwapRequest(id, swapRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteSwapRequest(@PathVariable Long id) {
        swapRequestService.deleteSwapRequest(id);
    }
}