package eu.senla.services;

import eu.senla.dto.CategoryDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {
    @PreAuthorize("hasAuthority('read')")
    List<CategoryDto> getAll();
    @PreAuthorize("hasAuthority('read')")
    CategoryDto getById(Integer id);
    @PreAuthorize("hasAuthority('write')")
    void create(CategoryDto categoryDto);
    @PreAuthorize("hasAuthority('write')")
    CategoryDto update(Integer id, CategoryDto categoryDto);
    @PreAuthorize("hasAuthority('write')")
    boolean delete(CategoryDto categoryDto);
    @PreAuthorize("hasAuthority('write')")
    boolean deleteById(Integer id);
}
