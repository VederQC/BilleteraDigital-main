package com.example.mscategories.Controller;

import com.example.mscategories.DTO.CategoryRequestDTO;
import com.example.mscategories.DTO.CategoryResponseDTO;
import com.example.mscategories.DTO.SubcategoryRequestDTO;
import com.example.mscategories.DTO.SubcategoryResponseDTO;
import com.example.mscategories.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(categoryService.getCategoriesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // Subcategorías
    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<SubcategoryResponseDTO> createSubcategory(
            @PathVariable Long categoryId,
            @RequestBody SubcategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.createSubcategory(categoryId, request));
    }

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<List<SubcategoryResponseDTO>> getSubcategories(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getSubcategories(categoryId));
    }

    @GetMapping("/{categoryId}/subcategories/{subId}")
    public ResponseEntity<SubcategoryResponseDTO> getSubcategoryById(
            @PathVariable Long categoryId, @PathVariable Long subId) {
        return ResponseEntity.ok(categoryService.getSubcategoryById(categoryId, subId));
    }
}
