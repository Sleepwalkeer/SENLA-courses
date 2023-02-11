package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dto.CategoryDto;
import eu.senla.entities.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;


    public List<CategoryDto> getAll() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        List<Category> categories = categoryDao.findAll();

        for (Category category : categories) {
            categoryDtoList.add(modelMapper.map(category, CategoryDto.class));
        }
        return categoryDtoList;
    }

    public CategoryDto getById(Integer id) {
        return modelMapper.map(categoryDao.findById(id), CategoryDto.class);
    }

    public CategoryDto update(CategoryDto categoryDto) {
        return modelMapper.map(categoryDao.update(modelMapper.map(categoryDto, Category.class)), CategoryDto.class);
    }

    public void create(CategoryDto categoryDto) {
        categoryDao.save(modelMapper.map(categoryDto, Category.class));
    }

    public void delete(CategoryDto categoryDto) {
        categoryDao.delete(modelMapper.map(categoryDto, Category.class));
    }
}
