package com.example.mswallet.Feign;

import com.example.mswallet.Dto.GoalResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "ms-goals-service", path = "/goals")
public interface GoalFeignClient {

    @PutMapping("/{goalId}/amount")
    GoalResponseDTO updateGoalAmount(
            @PathVariable Long goalId,
            @RequestParam("amountChange") BigDecimal amountChange
    );
}
