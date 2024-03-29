package eu.senla.service.implementation;

import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.dto.credentialsDto.ResponseCredentialsDto;
import eu.senla.entity.Credentials;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.CredentialsRepository;
import eu.senla.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final ModelMapper modelMapper;


    public ResponseCredentialsDto getById(Long id) {
        Credentials credentials = credentialsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " were found"));
        if (credentials.isDeleted()) {
            throw new NotFoundException("The credentials with ID " + id + "has been deleted");
        }
        return modelMapper.map(credentials, ResponseCredentialsDto.class);
    }

    @Transactional
    public void create(CredentialsDto credentialsDto) {
        Credentials credentials = modelMapper.map(credentialsDto, Credentials.class);
        credentialsRepository.save(credentials);
    }

    @Transactional
    public void update(Long id, CredentialsDto credentialsDto) {
        credentialsRepository.findById(id)
                .filter(creds -> !creds.isDeleted())
                .orElseThrow(() -> new NotFoundException("No credentials with ID " + id + " was found"));
        Credentials credentials = modelMapper.map(credentialsDto, Credentials.class);
        credentialsRepository.save(credentials);
    }

    @Transactional
    public void deleteById(Long id) {
        if (credentialsRepository.existsById(id)) {
            credentialsRepository.deleteById(id);
        } else {
            throw new NotFoundException("No credentials with ID " + id + " were found");
        }
    }

}
