package com.example.ShiftPlanner.model;

import jakarta.persistence.*;

@Entity
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee requester;

    @ManyToOne
    private Employee colleague;

    @ManyToOne
    private Shift shift;

    private String colleagueStatus;

    private String managerStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getRequester() {
        return requester;
    }

    public void setRequester(Employee requester) {
        this.requester = requester;
    }

    public Employee getColleague() {
        return colleague;
    }

    public void setColleague(Employee colleague) {
        this.colleague = colleague;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getColleagueStatus() {
        return colleagueStatus;
    }

    public void setColleagueStatus(String colleagueStatus) {
        this.colleagueStatus = colleagueStatus;
    }

    public String getManagerStatus() {
        return managerStatus;
    }

    public void setManagerStatus(String managerStatus) {
        this.managerStatus = managerStatus;
    }
}