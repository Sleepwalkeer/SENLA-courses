package eu.senla.controllers;

import eu.senla.dto.CategoryDto;
import eu.senla.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.create(categoryDto);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write')")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')")
    public void deleteCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.delete(categoryDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<CategoryDto> getAllCategorys() {
        return categoryService.getAll();
    }
}
