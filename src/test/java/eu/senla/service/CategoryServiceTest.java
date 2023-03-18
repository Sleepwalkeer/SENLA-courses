package eu.senla.service;

import eu.senla.dto.categoryDto.CategoryDto;
import eu.senla.dto.categoryDto.CreateCategoryDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.entity.Category;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.CategoryRepository;
import eu.senla.service.implementation.CategoryServiceImpl;
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
        CreateCategoryDto categoryDto = CreateCategoryDto.builder().name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
        categoryService.create(categoryDto);

        verify(categoryRepository).save(category);
    }

    @Test
    public void getByIdTest() {
        Category category = Category.builder().name("Construction tools").build();
        ResponseCategoryDto categoryDto = ResponseCategoryDto.builder().name("Construction tools").build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(modelMapper.map(category, ResponseCategoryDto.class)).thenReturn(categoryDto);

        ResponseCategoryDto categoryDtoRetrieved = categoryService.getById(1L);

        verify(categoryRepository).findById(1L);
        Assertions.assertNotNull(categoryDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.getById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Category category = Category.builder().name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        ResponseCategoryDto categoryDtoRetrieved = categoryService.update(1L, categoryDto);

        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).save(category);
    }

    @Test
    public void updateNonExistentCategoryTest() {
        Category category = Category.builder().id(1L).name("Construction tools").build();
        CategoryDto categoryDto = CategoryDto.builder().id(1L).name("Construction tools").build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);

        Assertions.assertThrows(NotFoundException.class, () -> categoryService.update(1L, categoryDto));
        verify(categoryRepository).existsById(1L);
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(categoryRepository).deleteById(1L);
        when(categoryRepository.existsById(1L)).thenReturn(true);
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
        Page<Category> categoryPage = new PageImpl<>(categories, paging, categories.size());

        when(categoryRepository.findAll(paging)).thenReturn(categoryPage);
        when(modelMapper.map(eq(category1), eq(CategoryDto.class)))
                .thenReturn(categoryDto1);
        when(modelMapper.map(eq(category2), eq(CategoryDto.class)))
                .thenReturn(categoryDto2);

        categoryService.getAll(1, 2, "id");

        verify(categoryRepository).findAll(paging);
    }
}
