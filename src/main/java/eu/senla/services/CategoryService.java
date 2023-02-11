package eu.senla.services;

import eu.senla.dto.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface CategoryService {
    List<CategoryDto> getAll();

    CategoryDto getById(Integer id);

    void create(CategoryDto accountDto);

    CategoryDto update(CategoryDto accountDto);

    void delete(CategoryDto accountDto);
}
