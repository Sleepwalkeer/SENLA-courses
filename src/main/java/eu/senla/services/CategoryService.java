package eu.senla.services;

import eu.senla.dto.CategoryDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {

    List<CategoryDto> getAll();

    CategoryDto getById(Integer id);

    void create(CategoryDto categoryDto);

    CategoryDto update(Integer id, CategoryDto categoryDto);

    boolean delete(CategoryDto categoryDto);

    boolean deleteById(Integer id);
}
