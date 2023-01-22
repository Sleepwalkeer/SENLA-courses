package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.AccountDto;
import eu.senla.services.AccountService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    public AccountController(AccountService accountService, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.objectMapper = objectMapper;
    }

    public List<String> getAll() throws JsonProcessingException {
        List<AccountDto> accountDtoList = accountService.getAll();
        List<String> accountJsonList = new ArrayList<>();
        for (AccountDto accountDto : accountDtoList) {
            accountJsonList.add(fromDtoToJson(accountDto));
        }
        return accountJsonList;
    }

    public String getById(String accountData) throws JsonProcessingException {
        return fromDtoToJson(accountService.getById(fromJsonToDto(accountData)));
    }

    public String update(String accountData, String phoneCode) throws JsonProcessingException {
        return fromDtoToJson(accountService.update(fromJsonToDto(accountData), phoneCode));
    }

    public String create(String accountData) throws JsonProcessingException {
        return fromDtoToJson(accountService.create(fromJsonToDto(accountData)));
    }

    public void delete(String accountData) throws JsonProcessingException {
        accountService.delete(fromJsonToDto(accountData));
    }

    private AccountDto fromJsonToDto(String accountJson) throws JsonProcessingException {
        return objectMapper.readValue(accountJson, AccountDto.class);
    }

    private String fromDtoToJson(AccountDto accountDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(accountDto);
    }
}
