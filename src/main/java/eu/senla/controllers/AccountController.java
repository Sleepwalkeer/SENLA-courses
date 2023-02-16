package eu.senla.controllers;

import eu.senla.dto.AccountDto;
import eu.senla.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    // private final ObjectMapper objectMapper;


    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Integer id) {
        AccountDto accountDto = accountService.getById(id);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AccountDto accountDto) {
        accountService.create(accountDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Integer id, @RequestBody AccountDto accountDto) {
        AccountDto updatedAccountDto = accountService.update(id, accountDto);
        if (updatedAccountDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAccountDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Integer id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestBody AccountDto accountDto) {
        accountService.delete(accountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtos = accountService.getAll();
        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/hello")
    public String HelloWorld() {
        return "Hello World";
    }
}
