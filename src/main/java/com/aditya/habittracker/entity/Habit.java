package com.aditya.habittracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;   
import java.sql.Timestamp;  

@Entity(name = "habits")
@Getter
@Setter
@ToString
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String habitName;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}
