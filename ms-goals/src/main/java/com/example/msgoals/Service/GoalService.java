package com.example.msgoals.Service;

import com.example.msgoals.DTO.AuthUserDto;
import com.example.msgoals.DTO.GoalRequestDTO;
import com.example.msgoals.DTO.GoalResponseDTO;
import com.example.msgoals.Entity.Goal;
import com.example.msgoals.Exceptions.ResourceNotFoundException;
import com.example.msgoals.Feign.UserFeignClient;
import com.example.msgoals.Repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserFeignClient userFeignClient;

    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO request) {
        try {
            AuthUserDto user = userFeignClient.getUserById(request.getUserId());
            if (user == null) throw new ResourceNotFoundException("Usuario no encontrado");
        } catch (Exception e) {
            throw new ResourceNotFoundException("Usuario no encontrado o ms-auth no disponible");
        }

        Goal goal = Goal.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(request.getCurrentAmount())
                .progress(calculateProgress(request.getCurrentAmount(), request.getTargetAmount()))
                .deadline(request.getDeadline())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        Goal saved = goalRepository.save(goal);
        return mapToResponse(saved);
    }

    public List<GoalResponseDTO> getGoalsByUser(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalResponseDTO updateGoalAmount(Long goalId, BigDecimal amountChange) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta no encontrada"));

        // Actualizar currentAmount según el gasto/ingreso
        goal.setCurrentAmount(goal.getCurrentAmount().add(amountChange));
        // Evitar que currentAmount sea negativo
        if (goal.getCurrentAmount().compareTo(BigDecimal.ZERO) < 0) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }

        goal.setProgress(calculateProgress(goal.getCurrentAmount(), goal.getTargetAmount()));

        Goal updated = goalRepository.save(goal);
        return mapToResponse(updated);
    }

    private BigDecimal calculateProgress(BigDecimal current, BigDecimal target) {
        if (target.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return current.multiply(BigDecimal.valueOf(100)).divide(target, 2, RoundingMode.HALF_UP);
    }

    private GoalResponseDTO mapToResponse(Goal goal) {
        return GoalResponseDTO.builder()
                .id(goal.getId())
                .userId(goal.getUserId())
                .name(goal.getName())
                .description(goal.getDescription())
                .targetAmount(goal.getTargetAmount())
                .currentAmount(goal.getCurrentAmount())
                .progress(goal.getProgress())
                .deadline(goal.getDeadline())
                .status(goal.getStatus())
                .createdAt(goal.getCreatedAt())
                .build();
    }
}
