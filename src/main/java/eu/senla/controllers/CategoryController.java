package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.CategoryDto;
import eu.senla.services.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    public CategoryController(CategoryService categoryService, ObjectMapper objectMapper) {
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
    }

    public List<String> getAll() throws JsonProcessingException {
        List<CategoryDto> categoryDtoList = categoryService.getAll();
        List<String> categoryJsonList = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtoList) {
            categoryJsonList.add(fromDtoToJson(categoryDto));
        }
        return categoryJsonList;
    }

    public String getById(String categoryData) throws JsonProcessingException {
        return fromDtoToJson(categoryService.getById(fromJsonToDto(categoryData)));
    }

    public String update(String categoryData) throws JsonProcessingException {
        return fromDtoToJson(categoryService.update(fromJsonToDto(categoryData)));
    }

    public void create(String categoryData) throws JsonProcessingException {
        categoryService.create(fromJsonToDto(categoryData));
    }

    public void delete(String categoryData) throws JsonProcessingException {
        categoryService.delete(fromJsonToDto(categoryData));
    }

    private CategoryDto fromJsonToDto(String categoryJson) throws JsonProcessingException {
        return objectMapper.readValue(categoryJson, CategoryDto.class);
    }

    private String fromDtoToJson(CategoryDto categoryDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(categoryDto);
    }
}
