package com.aditya.habittracker.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import jakarta.persistence.*;   
@Entity(name = "habit_status")
@Getter
@Setter
public class HabitStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    private LocalDate date;
    private Boolean completed;

}
