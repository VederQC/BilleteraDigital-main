package com.example.msgoals.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoalUpdateDTO {
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private LocalDateTime deadline;
    private String status;
}
