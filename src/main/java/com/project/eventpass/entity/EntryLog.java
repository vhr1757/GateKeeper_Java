package com.project.eventpass.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "entry_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entryTime;
    private String status;

    @ManyToOne
    @JoinColumn(name = "pass_id")
    private Pass pass;
}
