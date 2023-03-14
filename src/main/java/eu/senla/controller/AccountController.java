package eu.senla.controller;

import eu.senla.dto.AccountDto;
import eu.senla.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public AccountDto getAccountById(@PathVariable Long id) {

        log.info("Incoming request: GET /accounts/" + id);
        AccountDto response = accountService.getById(id);
        log.info("Outgoing response: {}", response);
        return response;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('read')")
    public void createAccount(@Valid @RequestBody AccountDto accountDto) {
        accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public AccountDto updateAccount(@PathVariable Long id,@Valid @RequestBody AccountDto accountDto) {
        return accountService.update(id, accountDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public void deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')|| #accountDto.id == authentication.principal.id")
    public void deleteAccount(@Valid @RequestBody AccountDto accountDto) {
        accountService.delete(accountDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<AccountDto> getAllAccounts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return accountService.getAll(pageNo, pageSize, sortBy);
    }
}
