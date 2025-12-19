package com.example.SP26SE025.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MenstrualCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;   // Ngày bắt đầu kỳ kinh
    private int cycleLength;       // Chu kỳ (thường là 28 ngày)
    private int periodLength;      // Số ngày hành kinh

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public MenstrualCycle() {
    }

    public MenstrualCycle(Long id, LocalDate startDate, int cycleLength, int periodLength, User user) {
        this.id = id;
        this.startDate = startDate;
        this.cycleLength = cycleLength;
        this.periodLength = periodLength;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(int periodLength) {
        this.periodLength = periodLength;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Optional: toString(), equals(), hashCode() if needed
}
