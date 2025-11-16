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
    private final UserFeignClient userFeignClient;

    // ------------------------------------------------------------
    // 🔹 Crear categoría (validando primero que el usuario exista)
    // ------------------------------------------------------------
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {

        // Validamos contra ms-auth usando Feign
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

        return mapToResponse(categoryRepository.save(category));
    }

    // ------------------------------------------------------------
    // 🔹 Obtener categorías por ID de usuario
    // ------------------------------------------------------------
    public List<CategoryResponseDTO> getCategoriesByUser(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------
    // 🔹 Obtener categoría por ID
    // ------------------------------------------------------------
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        return mapToResponse(category);
    }

    // ------------------------------------------------------------
    // 🔹 Actualizar una categoría
    // ------------------------------------------------------------
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {

        // Buscar categoría
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        // Actualizar campos
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setColor(request.getColor());

        return mapToResponse(categoryRepository.save(category));
    }

    // ------------------------------------------------------------
    // 🔹 Eliminar categoría
    //    Incluye BORRADO DE TODAS SUS SUBCATEGORÍAS
    // ------------------------------------------------------------
    @Transactional
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        // Eliminar subcategorías primero
        subcategoryRepository.deleteAll(
                subcategoryRepository.findByCategoryId(id)
        );

        // Eliminar categoría
        categoryRepository.delete(category);
    }

    // ------------------------------------------------------------
    // 🔹 Crear subcategoría dentro de una categoría
    // ------------------------------------------------------------
    @Transactional
    public SubcategoryResponseDTO createSubcategory(Long categoryId, SubcategoryRequestDTO request) {

        // Verificamos que la categoría exista
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Subcategory sub = Subcategory.builder()
                .category(category)
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();

        return mapSubcategory(subcategoryRepository.save(sub));
    }

    // ------------------------------------------------------------
    // 🔹 Listar todas las subcategorías de una categoría
    // ------------------------------------------------------------
    public List<SubcategoryResponseDTO> getSubcategories(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapSubcategory)
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------
    // 🔹 Obtener subcategoría por ID dentro de una categoría
    // ------------------------------------------------------------
    public SubcategoryResponseDTO getSubcategoryById(Long categoryId, Long subId) {
        Subcategory sub = subcategoryRepository.findById(subId)
                .filter(s -> s.getCategory().getId().equals(categoryId)) // Validación adicional
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));
        return mapSubcategory(sub);
    }

    // ------------------------------------------------------------
    // 🔹 Actualizar subcategoría
    // ------------------------------------------------------------
    @Transactional
    public SubcategoryResponseDTO updateSubcategory(Long categoryId, Long subId, SubcategoryRequestDTO request) {

        Subcategory sub = subcategoryRepository.findById(subId)
                .filter(s -> s.getCategory().getId().equals(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        sub.setName(request.getName());

        return mapSubcategory(subcategoryRepository.save(sub));
    }

    // ------------------------------------------------------------
    // 🔹 Eliminar subcategoría
    // ------------------------------------------------------------
    @Transactional
    public void deleteSubcategory(Long categoryId, Long subId) {

        Subcategory sub = subcategoryRepository.findById(subId)
                .filter(s -> s.getCategory().getId().equals(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        subcategoryRepository.delete(sub);
    }

    // ------------------------------------------------------------
    // 🔹 Mapper de categoría
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 🔹 Mapper de subcategoría
    // ------------------------------------------------------------
    private SubcategoryResponseDTO mapSubcategory(Subcategory s) {
        return SubcategoryResponseDTO.builder()
                .id(s.getId())
                .categoryId(s.getCategory().getId())
                .name(s.getName())
                .createdAt(s.getCreatedAt())
                .build();
    }
}