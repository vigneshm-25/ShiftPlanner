package com.example.ShiftPlanner.services;

import com.example.ShiftPlanner.model.Shift;

import java.util.List;

public interface ShiftService {

    Shift saveShift(Shift shift);

    List<Shift> getAllShifts();

    Shift getShiftById(Long id);

    Shift updateShift(Long id, Shift shift);

    void deleteShift(Long id);
}