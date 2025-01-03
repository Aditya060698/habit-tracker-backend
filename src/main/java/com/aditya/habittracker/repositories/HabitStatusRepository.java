package com.aditya.habittracker.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aditya.habittracker.entity.HabitStatus;

public interface HabitStatusRepository extends JpaRepository<HabitStatus, Long> {

    Collection<HabitStatus> findByHabitId(Long id);
    Collection<HabitStatus> findByHabitIdAndDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);
    HabitStatus findByHabitIdAndDate(Long id, LocalDate date);
}

