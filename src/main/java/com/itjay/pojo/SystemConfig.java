package com.itjay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig {
    private Integer id; // Usually a single row table, so ID might be fixed (e.g., 1)
    private LocalDateTime courseSelectionStartTime;
    private LocalDateTime courseSelectionEndTime;
    // Add other config items as needed
} 