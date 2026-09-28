package com.example.ShiftPlanner.controller;

import com.example.ShiftPlanner.model.Roster;
import com.example.ShiftPlanner.services.RosterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rosters")
public class RosterController {

    private final RosterService rosterService;

    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @PostMapping
    public Roster saveRoster(@RequestBody Roster roster) {
        return rosterService.saveRoster(roster);
    }

    @GetMapping
    public List<Roster> getAllRosters() {
        return rosterService.getAllRosters();
    }

    @GetMapping("/{id}")
    public Roster getRosterById(@PathVariable Long id) {
        return rosterService.getRosterById(id);
    }

    @PutMapping("/{id}")
    public Roster updateRoster(
            @PathVariable Long id,
            @RequestBody Roster roster) {

        return rosterService.updateRoster(id, roster);
    }

    @DeleteMapping("/{id}")
    public void deleteRoster(@PathVariable Long id) {
        rosterService.deleteRoster(id);
    }
}