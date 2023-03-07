package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dto.CategoryDto;
import eu.senla.entities.Account;
import eu.senla.entities.Category;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.InsufficientPrivilegesException;
import eu.senla.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;


    public CategoryDto getById(Long id) {
        Category category = categoryDao.findById(id).orElseThrow(() ->
                new NotFoundException("No category with ID " + id + " was found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    public void create(CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            throw new BadRequestException("Category name is required");
        }
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryDao.save(category);
    }

    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryDao.findById(id).orElseThrow(() ->
                new NotFoundException("No category with ID " + id + " was found"));
        modelMapper.map(categoryDto, category);
        Category updatedCategory = categoryDao.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    public void deleteById(Long id) {
        categoryDao.deleteById(id);
    }

    public void delete(CategoryDto categoryDto) {
        categoryDao.delete(modelMapper.map(categoryDto, Category.class));
    }

    public List<CategoryDto> getAll() {
        try {
            List<Category> categorys = categoryDao.findAll();
            return categorys.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
