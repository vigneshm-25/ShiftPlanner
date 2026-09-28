package com.example.ShiftPlanner.services.impl;

import com.example.ShiftPlanner.model.Employee;
import com.example.ShiftPlanner.model.Shift;
import com.example.ShiftPlanner.model.SwapRequest;
import com.example.ShiftPlanner.repository.EmployeeRepository;
import com.example.ShiftPlanner.repository.ShiftRepository;
import com.example.ShiftPlanner.repository.SwapRequestRepository;
import com.example.ShiftPlanner.services.SwapRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwapRequestServiceImpl implements SwapRequestService {

    private final SwapRequestRepository swapRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public SwapRequestServiceImpl(
            SwapRequestRepository swapRequestRepository,
            EmployeeRepository employeeRepository,
            ShiftRepository shiftRepository) {

        this.swapRequestRepository = swapRequestRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public SwapRequest createSwapRequest(SwapRequest swapRequest) {

        Employee requester = employeeRepository
                .findById(swapRequest.getRequester().getId())
                .orElse(null);

        Employee colleague = employeeRepository
                .findById(swapRequest.getColleague().getId())
                .orElse(null);

        Shift shift = shiftRepository
                .findById(swapRequest.getShift().getId())
                .orElse(null);

        swapRequest.setRequester(requester);
        swapRequest.setColleague(colleague);
        swapRequest.setShift(shift);

        return swapRequestRepository.save(swapRequest);
    }

    @Override
    public List<SwapRequest> getAllSwapRequests() {
        return swapRequestRepository.findAll();
    }

    @Override
    public SwapRequest getSwapRequestById(Long id) {
        return swapRequestRepository.findById(id).orElse(null);
    }

    @Override
    public SwapRequest updateSwapRequest(
            Long id,
            SwapRequest swapRequest) {

        SwapRequest existingRequest =
                swapRequestRepository.findById(id).orElse(null);

        if (existingRequest != null) {

            Employee requester = employeeRepository
                    .findById(swapRequest.getRequester().getId())
                    .orElse(null);

            Employee colleague = employeeRepository
                    .findById(swapRequest.getColleague().getId())
                    .orElse(null);

            Shift shift = shiftRepository
                    .findById(swapRequest.getShift().getId())
                    .orElse(null);

            existingRequest.setRequester(requester);
            existingRequest.setColleague(colleague);
            existingRequest.setShift(shift);

            existingRequest.setColleagueStatus(
                    swapRequest.getColleagueStatus());

            existingRequest.setManagerStatus(
                    swapRequest.getManagerStatus());

            return swapRequestRepository.save(existingRequest);
        }

        return null;
    }

    @Override
    public void deleteSwapRequest(Long id) {
        swapRequestRepository.deleteById(id);
    }
}