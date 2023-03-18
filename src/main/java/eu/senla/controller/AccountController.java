package eu.senla.controller;

import eu.senla.dto.accountDto.CreateAccountDto;
import eu.senla.dto.accountDto.ResponseAccountDto;
import eu.senla.dto.accountDto.UpdateAccountDto;
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
    public ResponseAccountDto getAccountById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('read')")
    public ResponseAccountDto createAccount(@Valid @RequestBody CreateAccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public ResponseAccountDto updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateAccountDto accountDto) {
        return accountService.update(id, accountDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public void deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<ResponseAccountDto> getAllAccounts(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return accountService.getAll(pageNo, pageSize, sortBy);
    }
}
