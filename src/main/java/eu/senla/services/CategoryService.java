package eu.senla.services;

import eu.senla.dto.CategoryDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {

    List<CategoryDto> getAll();

    CategoryDto getById(Long id);

    void create(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void delete(CategoryDto categoryDto);

    void deleteById(Long id);
}
