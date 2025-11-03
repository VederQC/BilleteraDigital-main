package com.example.mswallet.Feign;

import com.example.mswallet.Dto.CategoryDTO;
import com.example.mswallet.Dto.SubcategoryDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "ms-categories-service", path = "/categories")
public interface CategoryFeignClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "categoryByIdCB", fallbackMethod = "fallbackCategoryById")
    CategoryDTO getCategoryById(@PathVariable("id") Long id);

    @GetMapping("/{categoryId}/subcategories/{subId}")
    @CircuitBreaker(name = "subcategoryByIdCB", fallbackMethod = "fallbackSubcategoryById")
    SubcategoryDTO getSubcategoryById(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subId") Long subcategoryId
    );

    @GetMapping("/user/{userId}")
    @CircuitBreaker(name = "categoriesByUserCB", fallbackMethod = "fallbackCategoriesByUser")
    List<CategoryDTO> getCategoriesByUser(@PathVariable("userId") Long userId);


    default CategoryDTO fallbackCategoryById(Long id, Throwable e) {
        System.err.println(" CircuitBreaker: ms-categories-service no disponible (CategoryID: " + id + ")");
        CategoryDTO dto = new CategoryDTO();
        dto.setId(0L);
        dto.setName("Categoría no disponible");
        return dto;
    }

    default SubcategoryDTO fallbackSubcategoryById(Long categoryId, Long subcategoryId, Throwable e) {
        System.err.println(" CircuitBreaker: ms-categories-service no disponible (SubcategoryID: " + subcategoryId + ")");
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(0L);
        dto.setName("Subcategoría no disponible");
        return dto;
    }

    default List<CategoryDTO> fallbackCategoriesByUser(Long userId, Throwable e) {
        System.err.println(" CircuitBreaker: ms-categories-service no disponible (UserID: " + userId + ")");
        return Collections.emptyList();
    }
}