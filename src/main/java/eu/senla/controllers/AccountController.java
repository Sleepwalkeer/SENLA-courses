package eu.senla.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.senla.dto.AccountDto;
import eu.senla.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
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

    @GetMapping("/hello")
    public String HelloWorld() {
        return "Hello World";
    }
    //@RestController
//@RequestMapping("/accounts")
//public class AccountController {
//
//    private final AccountService accountService;
//
//    public AccountController(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
//        AccountDto accountDto = accountService.getAccountById(id);
//        if (accountDto == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(accountDto);
//    }
//
//    @PostMapping
//    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
//        AccountDto createdAccountDto = accountService.createAccount(accountDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccountDto);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
//        AccountDto updatedAccountDto = accountService.updateAccount(id, accountDto);
//        if (updatedAccountDto == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(updatedAccountDto);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//        boolean deleted = accountService.deleteAccount(id);
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AccountDto>> getAllAccounts() {
//        List<AccountDto> accountDtos = accountService.getAllAccounts();
//}
//        return ResponseEntity.ok(accountDtos);
//    }
    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Integer id){
    log.info("received request /account" + id);
    return  accountService.getById(id);
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
