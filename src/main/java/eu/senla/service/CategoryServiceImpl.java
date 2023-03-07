package eu.senla.service;

import eu.senla.repository.CategoryRepository;
import eu.senla.dto.CategoryDto;
import eu.senla.entity.Category;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No category with ID " + id + " was found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    public void create(CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            throw new BadRequestException("Category name is required");
        }
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(category);
    }

    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No category with ID " + id + " was found"));
        modelMapper.map(categoryDto, category);
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public void delete(CategoryDto categoryDto) {
        categoryRepository.delete(modelMapper.map(categoryDto, Category.class));
    }

    public List<CategoryDto> getAll() {
        try {
            List<Category> categorys = categoryRepository.findAll();
            return categorys.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
