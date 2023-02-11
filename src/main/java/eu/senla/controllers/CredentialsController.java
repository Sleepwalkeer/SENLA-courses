package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.CredentialsDto;
import eu.senla.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final ObjectMapper objectMapper;


    public List<String> getAll() throws JsonProcessingException {
        List<CredentialsDto> credentialsDtoList = credentialsService.getAll();
        List<String> credentialsJsonList = new ArrayList<>();
        for (CredentialsDto credentialsDto : credentialsDtoList) {
            credentialsJsonList.add(fromDtoToJson(credentialsDto));
        }
        return credentialsJsonList;
    }

//    public String getById(String credentialsData) throws JsonProcessingException {
//        return fromDtoToJson(credentialsService.getById(fromJsonToDto(credentialsData)));
//    }
    @GetMapping("/{id}")
    public ResponseEntity<CredentialsDto> getById(@PathVariable Integer id){
        log.info("received request /account" + id);
        return  ResponseEntity.ok(credentialsService.getById(id));
    }

    public String update(String credentialsData) throws JsonProcessingException {
        return fromDtoToJson(credentialsService.update(fromJsonToDto(credentialsData)));
    }

    public void create(String credentialsData) throws JsonProcessingException {
        credentialsService.create(fromJsonToDto(credentialsData));
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
