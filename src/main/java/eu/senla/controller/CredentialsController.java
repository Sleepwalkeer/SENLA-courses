package eu.senla.controller;

import eu.senla.dto.CredentialsDto;
import eu.senla.service.CredentialsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credentials")
@Slf4j
@RequiredArgsConstructor
public class CredentialsController {
    private final CredentialsService credentialsService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public CredentialsDto getCredentialsById(@PathVariable Long id) {
        return credentialsService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createCredentials(@Valid  @RequestBody CredentialsDto credentialsDto) {
        credentialsService.create(credentialsDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public CredentialsDto updateCredentials(@PathVariable Long id, @Valid @RequestBody CredentialsDto credentialsDto) {
        return credentialsService.update(id, credentialsDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public void deleteCredentialsById(@PathVariable Long id) {
        credentialsService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')|| #credentialsDto.id == authentication.principal.id")
    public void deleteCredentials(@Valid @RequestBody CredentialsDto credentialsDto) {
        credentialsService.delete(credentialsDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<CredentialsDto> getAllCredentials(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        return credentialsService.getAll(pageNo, pageSize, sortBy);
    }
}
