package eu.senla.services;

import eu.senla.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll();

    CategoryDto getById(CategoryDto accountDto);

    CategoryDto create(CategoryDto accountDto);

    CategoryDto update(CategoryDto accountDto, String newName);

    void delete(CategoryDto accountDto);
}
