package eu.senla.services;

import eu.senla.dto.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public interface CategoryService {
    List<CategoryDto> getAll();

    CategoryDto getById(CategoryDto accountDto);

    void create(CategoryDto accountDto);

    CategoryDto update(CategoryDto accountDto);

    void delete(CategoryDto accountDto);
}
