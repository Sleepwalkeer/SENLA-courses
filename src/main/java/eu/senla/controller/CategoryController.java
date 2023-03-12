package eu.senla.controller;

import eu.senla.dto.CategoryDto;
import eu.senla.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
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
    public List<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return categoryService.getAll(pageNo, pageSize, sortBy);
    }
}
