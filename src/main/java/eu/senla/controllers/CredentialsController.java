package eu.senla.controllers;

import eu.senla.dto.CredentialsDto;
import eu.senla.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public CredentialsDto getCredentialsById(@PathVariable Integer id) {
        return credentialsService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createCredentials(@RequestBody CredentialsDto credentialsDto) {
        credentialsService.create(credentialsDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public CredentialsDto updateCredentials(@PathVariable Integer id, @RequestBody CredentialsDto credentialsDto) {
        return credentialsService.update(id, credentialsDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('write') || #id == authentication.principal.id")
    public void deleteCredentialsById(@PathVariable Integer id) {
        credentialsService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('write')|| #credentialsDto.id == authentication.principal.id")
    public void deleteCredentials(@RequestBody CredentialsDto credentialsDto) {
        credentialsService.delete(credentialsDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('write')")
    public List<CredentialsDto> getAllCredentialss() {
        return credentialsService.getAll();
    }
}
