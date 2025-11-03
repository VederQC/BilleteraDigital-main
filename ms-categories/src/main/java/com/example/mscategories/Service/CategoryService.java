package com.example.mscategories.Service;

import com.example.mscategories.DTO.*;
import com.example.mscategories.Entity.Category;
import com.example.mscategories.Entity.Subcategory;
import com.example.mscategories.Exceptions.ResourceNotFoundException;
import com.example.mscategories.Feign.UserFeignClient;
import com.example.mscategories.Repository.CategoryRepository;
import com.example.mscategories.Repository.SubcategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final UserFeignClient userFeignClient; // ✅ inyectamos el cliente Feign

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        try {
            AuthUserDto user = userFeignClient.getUserById(request.getUserId());
            if (user == null) {
                throw new ResourceNotFoundException("Usuario no encontrado");
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Usuario no encontrado o ms-auth no disponible");
        }

        Category category = Category.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .icon(request.getIcon())
                .color(request.getColor())
                .createdAt(LocalDateTime.now())
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    public List<CategoryResponseDTO> getCategoriesByUser(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return mapToResponse(category);
    }

    // ✅ Subcategorías
    @Transactional
    public SubcategoryResponseDTO createSubcategory(Long categoryId, SubcategoryRequestDTO request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Subcategory sub = Subcategory.builder()
                .category(category)
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Subcategory saved = subcategoryRepository.save(sub);
        return SubcategoryResponseDTO.builder()
                .id(saved.getId())
                .categoryId(category.getId())
                .name(saved.getName())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<SubcategoryResponseDTO> getSubcategories(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId)
                .stream()
                .map(sub -> SubcategoryResponseDTO.builder()
                        .id(sub.getId())
                        .categoryId(sub.getCategory().getId())
                        .name(sub.getName())
                        .createdAt(sub.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public SubcategoryResponseDTO getSubcategoryById(Long categoryId, Long subId) {
        Subcategory sub = subcategoryRepository.findById(subId)
                .filter(s -> s.getCategory().getId().equals(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));
        return SubcategoryResponseDTO.builder()
                .id(sub.getId())
                .categoryId(categoryId)
                .name(sub.getName())
                .createdAt(sub.getCreatedAt())
                .build();
    }

    private CategoryResponseDTO mapToResponse(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .userId(c.getUserId())
                .name(c.getName())
                .icon(c.getIcon())
                .color(c.getColor())
                .createdAt(c.getCreatedAt())
                .build();
    }
}

