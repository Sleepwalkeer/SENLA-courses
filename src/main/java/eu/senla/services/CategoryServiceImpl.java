package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dto.CategoryDto;
import eu.senla.entities.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper;


    public CategoryDto getById(Integer id) {
        Category category = categoryDao.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        return modelMapper.map(category, CategoryDto.class);
    }

    public void create(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryDao.save(category);
    }

    public CategoryDto update(Integer id, CategoryDto categoryDto) {
        Category category = categoryDao.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        modelMapper.map(categoryDto, category);
        Category updatedCategory = categoryDao.update(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    public boolean deleteById(Integer id) {
        return categoryDao.deleteById(id);
    }

    @Override
    public boolean delete(CategoryDto categoryDto) {
        return categoryDao.delete(modelMapper.map(categoryDto, Category.class));
    }

    public List<CategoryDto> getAll() {
        List<Category> categorys = categoryDao.findAll();
        return categorys.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
}
