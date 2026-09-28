package com.example.ShiftPlanner.services;

import com.example.ShiftPlanner.model.Roster;

import java.util.List;

public interface RosterService {

    Roster saveRoster(Roster roster);

    List<Roster> getAllRosters();

    Roster getRosterById(Long id);

    Roster updateRoster(Long id, Roster roster);

    void deleteRoster(Long id);
}