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

@RequiredArgsConstructor
@Service
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final ModelMapper modelMapper;


    public ResponseCredentialsDto getById(Long id) {
        Credentials credentials = credentialsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " was found"));


        return modelMapper.map(credentials, ResponseCredentialsDto.class);
    }

    public void create(CredentialsDto credentialsDto) {
        Credentials credentials = modelMapper.map(credentialsDto, Credentials.class);
        credentialsRepository.save(credentials);
    }

    public void update(Long id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " was found"));
        modelMapper.map(credentialsDto, credentials);
        credentialsRepository.save(credentials);
    }

    public void deleteById(Long id) {
        credentialsRepository.deleteById(id);
    }

}
