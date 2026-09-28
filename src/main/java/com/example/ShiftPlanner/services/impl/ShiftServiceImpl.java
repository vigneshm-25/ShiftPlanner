package com.example.ShiftPlanner.services.impl;

import com.example.ShiftPlanner.model.Shift;
import com.example.ShiftPlanner.repository.ShiftRepository;
import com.example.ShiftPlanner.services.ShiftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift saveShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Shift getShiftById(Long id) {
        return shiftRepository.findById(id).orElse(null);
    }

    @Override
    public Shift updateShift(Long id, Shift shift) {

        Shift existingShift = shiftRepository.findById(id).orElse(null);

        if (existingShift != null) {

            existingShift.setShiftDate(shift.getShiftDate());
            existingShift.setStartTime(shift.getStartTime());
            existingShift.setEndTime(shift.getEndTime());
            existingShift.setShiftType(shift.getShiftType());

            return shiftRepository.save(existingShift);
        }

        return null;
    }

    @Override
    public void deleteShift(Long id) {
        shiftRepository.deleteById(id);
    }
}