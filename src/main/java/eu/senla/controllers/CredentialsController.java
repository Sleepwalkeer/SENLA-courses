package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.CredentialsDto;
import eu.senla.services.CredentialsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final ObjectMapper objectMapper;

    public CredentialsController(CredentialsService credentialsService, ObjectMapper objectMapper) {
        this.credentialsService = credentialsService;
        this.objectMapper = objectMapper;
    }

    public List<String> getAll() throws JsonProcessingException {
        List<CredentialsDto> credentialsDtoList = credentialsService.getAll();
        List<String> credentialsJsonList = new ArrayList<>();
        for (CredentialsDto credentialsDto : credentialsDtoList) {
            credentialsJsonList.add(fromDtoToJson(credentialsDto));
        }
        return credentialsJsonList;
    }

    public String getById(String credentialsData) throws JsonProcessingException {
        return fromDtoToJson(credentialsService.getById(fromJsonToDto(credentialsData)));
    }

    public String update(String credentialsData, String newPassword) throws JsonProcessingException {
        return fromDtoToJson(credentialsService.update(fromJsonToDto(credentialsData), newPassword));
    }

    public String create(String credentialsData) throws JsonProcessingException {
        return fromDtoToJson(credentialsService.create(fromJsonToDto(credentialsData)));
    }

    public void delete(String credentialsData) throws JsonProcessingException {
        credentialsService.delete(fromJsonToDto(credentialsData));
    }

    private CredentialsDto fromJsonToDto(String credentialsJson) throws JsonProcessingException {
        return objectMapper.readValue(credentialsJson, CredentialsDto.class);
    }

    private String fromDtoToJson(CredentialsDto credentialsDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(credentialsDto);
    }
}
