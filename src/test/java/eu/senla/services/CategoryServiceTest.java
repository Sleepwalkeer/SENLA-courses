package eu.senla.services;

import eu.senla.dao.CategoryDao;
import eu.senla.dto.CategoryDto;
import eu.senla.entities.Category;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock
    private CategoryDao categoryDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Category category = Category.builder().id(1).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();

        doNothing().when(categoryDao).save(category);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        categoryService.create(categoryDto);

        verify(categoryDao).save(category);

    }

    @Test
    public void createWithInvalidDataTest() {
        CategoryDto categoryDto = CategoryDto.builder().build();
        Assertions.assertThrows(BadRequestException.class, () -> categoryService.create(categoryDto));
    }

    @Test
    public void getByIdTest() {
        Category category = Category.builder().id(1).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();
        when(categoryDao.findById(1)).thenReturn(Optional.ofNullable(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);

        CategoryDto categoryDtoRetrieved = categoryService.getById(1);

        verify(categoryDao).findById(1);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto, categoryDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(categoryDao.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.getById(1));
        verify(categoryDao).findById(1);
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().id(1).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();

        when(categoryDao.update(category)).thenReturn(category);
        when(categoryDao.findById(1)).thenReturn(Optional.ofNullable(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        CategoryDto categoryDtoRetrieved = categoryService.update(1, categoryDto);


        verify(categoryDao).findById(1);
        verify(categoryDao).update(category);
        Assertions.assertEquals(categoryDto, categoryDtoRetrieved);
    }

    @Test
    public void updateNonExistentCategoryTest() {
        Category category = Category.builder().id(1).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();

        when(categoryDao.update(category)).thenReturn(category);
        when(categoryDao.findById(1)).thenReturn(Optional.empty());
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.update(1, categoryDto));
        verify(categoryDao).findById(1);
    }


    @Test
    public void deleteTest() {
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();
        Category category = Category.builder().id(1).name("Construction tools").build();
        when(categoryDao.delete(category)).thenReturn(true);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        Assertions.assertTrue(categoryService.delete(categoryDto));
        verify(categoryDao).delete(category);
    }

    @Test
    public void deleteNonExistentCategoryTest() {
        CategoryDto categoryDto = CategoryDto.builder().id(1).name("Construction tools").build();
        Category category = Category.builder().id(1).name("Construction tools").build();
        when(categoryDao.delete(category)).thenReturn(false);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        Assertions.assertFalse(categoryService.delete(categoryDto));
        verify(categoryDao).delete(category);
    }

    @Test
    public void deleteByIdTest() {
        when(categoryDao.deleteById(1)).thenReturn(true);

        Assertions.assertTrue(categoryService.deleteById(1));
        verify(categoryDao).deleteById(1);
    }

    @Test
    public void deleteByNonExistentIdTest() {
        when(categoryDao.deleteById(1)).thenReturn(false);

        Assertions.assertFalse(categoryService.deleteById(1));
        verify(categoryDao).deleteById(1);
    }

    @Test
    public void getAllTest() {
        CategoryDto categoryDto1 = CategoryDto.builder().id(1).name("Construction tools").build();
        CategoryDto categoryDto2 = CategoryDto.builder().id(2).name("furniture").build();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryDtos.add(categoryDto1);
        categoryDtos.add(categoryDto2);

        Category category1 = Category.builder().id(1).name("Construction tools").build();
        Category category2 = Category.builder().id(2).name("furniture").build();
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryDao.findAll()).thenReturn(categories);
        when(modelMapper.map(eq(category1), eq(CategoryDto.class)))
                .thenReturn(categoryDto1);
        when(modelMapper.map(eq(category2), eq(CategoryDto.class)))
                .thenReturn(categoryDto2);

        List<CategoryDto> retrievedCategoryDtos = categoryService.getAll();

        verify(categoryDao).findAll();
        Assertions.assertIterableEquals(categoryDtos, retrievedCategoryDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        when(categoryDao.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> categoryService.getAll());
        verify(categoryDao).findAll();
    }
}
