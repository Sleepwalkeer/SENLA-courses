package eu.senla.service;

import eu.senla.repository.CategoryRepository;
import eu.senla.dto.CategoryDto;
import eu.senla.entity.Category;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
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
        Category category = Category.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
        categoryService.create(categoryDto);

        verify(categoryRepository).save(category);
    }

    @Test
    public void createWithInvalidDataTest() {
        CategoryDto categoryDto = CategoryDto.builder().build();
        Assertions.assertThrows(BadRequestException.class, () -> categoryService.create(categoryDto));
    }

    @Test
    public void getByIdTest() {
        Category category = Category.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);

        CategoryDto categoryDtoRetrieved = categoryService.getById(1L);

        verify(categoryRepository).findById(1L);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto, categoryDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.getById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        CategoryDto categoryDtoRetrieved = categoryService.update(1L, categoryDto);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(category);
        Assertions.assertEquals(categoryDto, categoryDtoRetrieved);
    }

    @Test
    public void updateNonExistentCategoryTest() {
        Category category = Category.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.update(1L, categoryDto));
        verify(categoryRepository).findById(1L);
    }


    @Test
    public void deleteTest() {
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();
        Category category = Category.builder().id(1L).name("Construction tools").build();
        doNothing().when(categoryRepository).delete(category);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
        categoryService.delete(categoryDto);
        verify(categoryRepository).delete(category);
    }


    @Test
    public void deleteByIdTest() {
        doNothing().when(categoryRepository).deleteById(1L);
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }


    @Test
    public void getAllTest() {
        CategoryDto categoryDto1 = CategoryDto.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto2 = CategoryDto.builder().id(2L).name("furniture").build();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryDtos.add(categoryDto1);
        categoryDtos.add(categoryDto2);

        Category category1 = Category.builder().id(1L).name("Construction tools").build();
        Category category2 = Category.builder().id(2L).name("furniture").build();
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Category> categoryPage = new PageImpl<>(categories,paging,categories.size());

        when(categoryRepository.findAll(paging)).thenReturn(categoryPage);
        when(modelMapper.map(eq(category1), eq(CategoryDto.class)))
                .thenReturn(categoryDto1);
        when(modelMapper.map(eq(category2), eq(CategoryDto.class)))
                .thenReturn(categoryDto2);

        List<CategoryDto> retrievedCategoryDtos = categoryService.getAll(1,2,"id");

        verify(categoryRepository).findAll(paging);
        Assertions.assertIterableEquals(categoryDtos, retrievedCategoryDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        when(categoryRepository.findAll(paging)).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> categoryService.getAll(1,2,"id"));
        verify(categoryRepository).findAll(paging);
    }
}
