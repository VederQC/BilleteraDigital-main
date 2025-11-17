package com.example.msgoals.Service;

import com.example.msgoals.DTO.*;
import com.example.msgoals.Entity.Goal;
import com.example.msgoals.Exceptions.ResourceNotFoundException;
import com.example.msgoals.Feign.UserFeignClient;
import com.example.msgoals.Feign.CategoryFeignClient;
import com.example.msgoals.Repository.GoalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserFeignClient userFeignClient;
    private final CategoryFeignClient categoryFeignClient;

    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO request) {

        // Crear subcategoría automática dentro de categoría Objetivos (id 6)
        SubcategoryResponseDTO sub = categoryFeignClient.createSubcategory(
                6L,
                new SubcategoryRequestDTO(
                        request.getName(),
                        "emoji_objects"
                )
        );

        // Crear la meta
        Goal goal = Goal.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(request.getCurrentAmount())
                .subcategoryId(sub.getId()) // guardar id real de subcategoría
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

        goal.setCurrentAmount(goal.getCurrentAmount().add(amountChange));

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
                .subcategoryId(goal.getSubcategoryId()) // 🔥 IMPORTANTE
                .deadline(goal.getDeadline())
                .status(goal.getStatus())
                .createdAt(goal.getCreatedAt())
                .build();
    }

    @Transactional
    public GoalResponseDTO updateGoal(Long goalId, GoalUpdateDTO request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta no encontrada"));

        if (request.getName() != null) goal.setName(request.getName());
        if (request.getDescription() != null) goal.setDescription(request.getDescription());
        if (request.getTargetAmount() != null) {
            goal.setTargetAmount(request.getTargetAmount());
            goal.setProgress(calculateProgress(goal.getCurrentAmount(), request.getTargetAmount()));
        }
        if (request.getDeadline() != null) goal.setDeadline(request.getDeadline());
        if (request.getStatus() != null) goal.setStatus(request.getStatus());

        Goal updated = goalRepository.save(goal);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteGoal(Long goalId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta no encontrada"));

        // Eliminar subcategoría asociada
        if (goal.getSubcategoryId() != null) {
            try {
                categoryFeignClient.deleteSubcategory(goal.getSubcategoryId());
            } catch (Exception ignored) {
                // Si falla categories, no romper ms-goals
            }
        }

        goalRepository.delete(goal);
    }

}
