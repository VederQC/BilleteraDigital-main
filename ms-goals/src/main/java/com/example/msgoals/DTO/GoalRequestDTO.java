package com.example.msgoals.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequestDTO {
    private Long userId;
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDateTime deadline;
    private String status; // Ejemplo: "EN_PROGRESO", "COMPLETADA", "CANCELADA"
}
