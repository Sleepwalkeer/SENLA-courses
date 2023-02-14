package eu.senla.controllers;

import eu.senla.dto.CredentialsDto;
import eu.senla.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/credentials")
public class CredentialsController {
    private final CredentialsService credentialsService;
    // private final ObjectMapper objectMapper;


    @GetMapping("/{id}")
    public ResponseEntity<CredentialsDto> getCredentialsById(@PathVariable Integer id) {
        CredentialsDto credentialsDto = credentialsService.getById(id);
        if (credentialsDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credentialsDto);
    }

    @PostMapping
    public ResponseEntity<Void> createCredentials(@RequestBody CredentialsDto credentialsDto) {
        credentialsService.create(credentialsDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialsDto> updateCredentials(@PathVariable Integer id, @RequestBody CredentialsDto credentialsDto) {
        CredentialsDto updatedCredentialsDto = credentialsService.update(id, credentialsDto);
        if (updatedCredentialsDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCredentialsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredentialsById(@PathVariable Integer id) {
        credentialsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCredentials(@RequestBody CredentialsDto credentialsDto) {
        credentialsService.delete(credentialsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CredentialsDto>> getAllCredentialss() {
        List<CredentialsDto> credentialsDtos = credentialsService.getAll();
        return ResponseEntity.ok(credentialsDtos);
    }
}
