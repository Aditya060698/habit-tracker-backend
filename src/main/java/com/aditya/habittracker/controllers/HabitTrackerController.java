package com.aditya.habittracker.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.habittracker.entity.Habit;
import com.aditya.habittracker.entity.HabitStatus;
import com.aditya.habittracker.entity.User;
import com.aditya.habittracker.repositories.HabitRepository;
import com.aditya.habittracker.repositories.HabitStatusRepository;
import com.aditya.habittracker.repositories.UserRepository;

@RestController
@RequestMapping("/api")
public class HabitTrackerController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitStatusRepository habitStatusRepository;

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/habits")
    public Habit createHabit(@RequestBody Habit habit) {
        return habitRepository.save(habit);
    }

    @PostMapping("/habit-status")
    public HabitStatus updateHabitStatus(@RequestBody HabitStatus habitStatus) {
        return habitStatusRepository.save(habitStatus);
    }

    @GetMapping("/habits/{userId}")
    public List<Habit> getUserHabits(@PathVariable Long userId) {
        return habitRepository.findAllByUserId(userId);
    }
    @GetMapping("/habits/{userId}/status")
    public List<HabitStatus> getUserHabitsStatus(@PathVariable Long userId, @RequestParam String date) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        List<HabitStatus> response=new ArrayList<>();
        for (Habit habit : habits) {
            HabitStatus status = habitStatusRepository.findByHabitIdAndDate(habit.getId(),LocalDate.parse(date));
            response.add(status);
        }
        return response;
    }


    @GetMapping("/analytics/{userId}")
    public List<Map<String, Object>> getAnalytics(@PathVariable Long userId) {
        List<Habit> habits = habitRepository.findAllByUserId(userId);
        List<Map<String, Object>> analytics = new ArrayList<>();

        for (Habit habit : habits) {
            List<HabitStatus> statuses = habitStatusRepository.findByHabitId(habit.getId()).stream().toList();
            long completed = statuses.stream().filter(HabitStatus::getCompleted).count();
            long total = statuses.size();
            double completionRate = total > 0 ? (completed * 100.0 / total) : 0;

            Map<String, Object> stat = new HashMap<>();
            stat.put("habitName", habit.getHabitName());
            stat.put("completionRate", completionRate);
            analytics.add(stat);
        }

        return analytics;
    }

    @PostMapping("habits/{habitId}/status")
    public ResponseEntity<?> logHabitStatus(
            @PathVariable Long habitId,
            @RequestBody Map<String, Object> statusData) {
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
                System.out.println(habit.toString());
        LocalDate date = LocalDate.parse((String) statusData.get("date"));
        boolean completed = (boolean) statusData.get("completed");

        HabitStatus habitStatus = habitStatusRepository
                .findByHabitIdAndDateBetween(habitId, date, date)
                .stream()
                .findFirst()
                .orElse(new HabitStatus());

        habitStatus.setHabit(habit);
        habitStatus.setDate(date);
        habitStatus.setCompleted(completed);

        habitStatusRepository.save(habitStatus);

        return ResponseEntity.ok("Habit status updated!");
    }

    @GetMapping("habits/{habitId}/history")
    public List<HabitStatus> getHabitHistory(
            @PathVariable Long habitId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return habitStatusRepository.findByHabitIdAndDateBetween(
                habitId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)).stream().toList();
    }
}
