package eu.senla.service.implementation;

import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.dto.categoryDto.CategoryDto;
import eu.senla.dto.categoryDto.CreateCategoryDto;
import eu.senla.dto.categoryDto.ResponseCategoryDto;
import eu.senla.entity.Category;
import eu.senla.entity.Category;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.CategoryRepository;
import eu.senla.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    public ResponseCategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No category with ID " + id + " was found"));
        return modelMapper.map(category, ResponseCategoryDto.class);
    }

    @Transactional
    public ResponseCategoryDto create(CreateCategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category, ResponseCategoryDto.class);
    }

    @Transactional
    public ResponseCategoryDto update(Long id, CategoryDto categoryDto) {
        if(categoryRepository.existsById(id)){
            Category category = modelMapper.map(categoryDto, Category.class);
            Category  updatedCategory = categoryRepository.save(category);
            return modelMapper.map(updatedCategory, ResponseCategoryDto.class);
        }
        else {
            throw  new NotFoundException("No category with ID " + id + " was found");
        }
    }

    @Transactional
    public void deleteById(Long id){
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new NotFoundException("No category with ID " + id + " was found");
        }
    }

    public List<ResponseCategoryDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Category> categoryPage = categoryRepository.findAll(paging);

        return categoryPage.getContent()
                .stream()
                .map(category -> modelMapper.map(category, ResponseCategoryDto.class)).collect(Collectors.toList());
    }
}
