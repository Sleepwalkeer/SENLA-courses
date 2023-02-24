package eu.senla.controllers;

import eu.senla.dto.AccountDto;
import eu.senla.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public AccountDto getAccountById(@PathVariable Integer id) {
        return accountService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createAccount(@RequestBody AccountDto accountDto) {
        accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public AccountDto updateAccount(@PathVariable Integer id, @RequestBody AccountDto accountDto) {
        return accountService.update(id, accountDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public void deleteAccountById(@PathVariable Integer id) {
        accountService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')|| #accountDto.id == authentication.principal.id")
    public void deleteAccount(@RequestBody AccountDto accountDto) {
        accountService.delete(accountDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<AccountDto> getAllAccounts() {
        return accountService.getAll();
    }
}
