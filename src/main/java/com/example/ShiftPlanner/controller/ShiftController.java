package com.example.ShiftPlanner.controller;

import com.example.ShiftPlanner.model.Shift;
import com.example.ShiftPlanner.services.ShiftService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping
    public Shift saveShift(@RequestBody Shift shift) {
        return shiftService.saveShift(shift);
    }

    @GetMapping
    public List<Shift> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @GetMapping("/{id}")
    public Shift getShiftById(@PathVariable Long id) {
        return shiftService.getShiftById(id);
    }

    @PutMapping("/{id}")
    public Shift updateShift(
            @PathVariable Long id,
            @RequestBody Shift shift) {

        return shiftService.updateShift(id, shift);
    }

    @DeleteMapping("/{id}")
    public void deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
    }
}