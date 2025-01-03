package com.aditya.habittracker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aditya.habittracker.entity.Habit;


public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findAllByUserId(Long userId);}

