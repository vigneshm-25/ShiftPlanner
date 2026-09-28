package com.example.ShiftPlanner.services.impl;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.ShiftPlanner.model.Employee;
import com.example.ShiftPlanner.model.Roster;
import com.example.ShiftPlanner.model.Shift;
import com.example.ShiftPlanner.repository.EmployeeRepository;
import com.example.ShiftPlanner.repository.RosterRepository;
import com.example.ShiftPlanner.repository.ShiftRepository;
import com.example.ShiftPlanner.services.RosterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RosterServiceImpl implements RosterService {

    private final RosterRepository rosterRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public RosterServiceImpl(
            RosterRepository rosterRepository,
            EmployeeRepository employeeRepository,
            ShiftRepository shiftRepository) {

        this.rosterRepository = rosterRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Roster saveRoster(Roster roster) {

        Employee employee = employeeRepository
                .findById(roster.getEmployee().getId())
                .orElse(null);

        Shift shift = shiftRepository
                .findById(roster.getShift().getId())
                .orElse(null);

        // Check existing rosters of this employee
        List<Roster> existingRosters = rosterRepository.findByEmployee(employee);

        for (Roster existingRoster : existingRosters) {

            Shift existingShift = existingRoster.getShift();

            // Check if shifts are on the same date
            if (existingShift.getShiftDate().equals(shift.getShiftDate())) {

                // Check if times overlap
                if (existingShift.getStartTime().isBefore(shift.getEndTime())
                        && existingShift.getEndTime().isAfter(shift.getStartTime())) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Employee already has an overlapping shift on this date."
                    );
                }
            }
        }

        roster.setEmployee(employee);
        roster.setShift(shift);

        return rosterRepository.save(roster);
    }

    @Override
    public List<Roster> getAllRosters() {
        return rosterRepository.findAll();
    }

    @Override
    public Roster getRosterById(Long id) {
        return rosterRepository.findById(id).orElse(null);
    }

    @Override
    public Roster updateRoster(Long id, Roster roster) {

        Roster existingRoster = rosterRepository
                .findById(id)
                .orElse(null);

        if (existingRoster != null) {

            Employee employee = employeeRepository
                    .findById(roster.getEmployee().getId())
                    .orElse(null);

            Shift shift = shiftRepository
                    .findById(roster.getShift().getId())
                    .orElse(null);

            existingRoster.setEmployee(employee);
            existingRoster.setShift(shift);
            existingRoster.setWeekStartDate(roster.getWeekStartDate());

            return rosterRepository.save(existingRoster);
        }

        return null;
    }

    @Override
    public void deleteRoster(Long id) {
        rosterRepository.deleteById(id);
    }
}