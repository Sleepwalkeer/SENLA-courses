package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dto.CategoryDto;
import eu.senla.entities.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryService {
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryDao categoryDao, ModelMapper modelMapper) {
        this.categoryDao = categoryDao;
        this.modelMapper = modelMapper;
    }
    public List<CategoryDto> getAll() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        List<Category> categories = categoryDao.getAll();

        for (Category category : categories) {
            categoryDtoList.add(fromEntityToDto(category));
        }
        return categoryDtoList;
    }

    public CategoryDto getById(CategoryDto categoryDto) {
        return fromEntityToDto(categoryDao.getById(fromDtoToEntity(categoryDto)));
    }

    public CategoryDto update(CategoryDto categoryDto, String newName) {
        return fromEntityToDto(categoryDao.update(fromDtoToEntity(categoryDto), newName));
    }

    public CategoryDto create(CategoryDto categoryDto) {
        return fromEntityToDto(categoryDao.create(fromDtoToEntity(categoryDto)));
    }

    public void delete(CategoryDto categoryDto) {
        categoryDao.delete(fromDtoToEntity(categoryDto));
    }

    private Category fromDtoToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto fromEntityToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
