package eu.senla.controller;

import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.dto.credentialsDto.ResponseCredentialsDto;
import eu.senla.service.CredentialsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credentials")
@Slf4j
@RequiredArgsConstructor
public class CredentialsController {
    private final CredentialsService credentialsService;


    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseCredentialsDto getCredentialsById(@PathVariable Long id) {
        return credentialsService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write')")
    public void createCredentials(@Valid @RequestBody CredentialsDto credentialsDto) {
        credentialsService.create(credentialsDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public void updateCredentials(@PathVariable Long id, @Valid @RequestBody CredentialsDto credentialsDto) {
        credentialsService.update(id, credentialsDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public void deleteCredentialsById(@PathVariable Long id) {
        credentialsService.deleteById(id);
    }

}
