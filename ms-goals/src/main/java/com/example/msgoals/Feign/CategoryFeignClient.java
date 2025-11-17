package com.example.msgoals.Feign;

import com.example.msgoals.DTO.SubcategoryRequestDTO;
import com.example.msgoals.DTO.SubcategoryResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-categories-service", path = "/categories")
public interface CategoryFeignClient {

    @PostMapping("/{categoryId}/subcategories")
    SubcategoryResponseDTO createSubcategory(
            @PathVariable Long categoryId,
            @RequestBody SubcategoryRequestDTO request
    );

    @DeleteMapping("/subcategories/{subcategoryId}")
    void deleteSubcategory(@PathVariable Long subcategoryId);
}

