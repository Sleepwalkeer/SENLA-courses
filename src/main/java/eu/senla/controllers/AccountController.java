package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.AccountDto;
import eu.senla.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;


    public List<String> getAll() throws JsonProcessingException {
        List<AccountDto> accountDtoList = accountService.getAll();
        List<String> accountJsonList = new ArrayList<>();
        for (AccountDto accountDto : accountDtoList) {
            accountJsonList.add(fromDtoToJson(accountDto));
        }
        return accountJsonList;
    }


    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Integer id){

    }
//    public String getById(String accountData) throws JsonProcessingException {
//        return fromDtoToJson(accountService.getById(fromJsonToDto(accountData)));
//    }

    public String update(String accountData) throws JsonProcessingException {
        return fromDtoToJson(accountService.update(fromJsonToDto(accountData)));
    }

    public void create(String accountData) throws JsonProcessingException {
         accountService.create(fromJsonToDto(accountData));
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
