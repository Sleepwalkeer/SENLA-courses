package eu.senla.service;

import eu.senla.dto.CredentialsDto;
import eu.senla.entity.Credentials;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final ModelMapper modelMapper;


    public CredentialsDto getById(Long id) {
        Credentials credentials = credentialsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " was found"));
        return modelMapper.map(credentials, CredentialsDto.class);
    }

    public void create(CredentialsDto credentialsDto) {
        if (credentialsDto.getPassword() == null || credentialsDto.getPassword().isEmpty()) {
            throw new BadRequestException("Password is required");
        }
        if (credentialsDto.getUsername() == null || credentialsDto.getUsername().isEmpty()) {
            throw new BadRequestException("Username is required");
        }
        Credentials credentials = modelMapper.map(credentialsDto, Credentials.class);
        credentialsRepository.save(credentials);
    }

    public CredentialsDto update(Long id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsRepository.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " was found"));
        modelMapper.map(credentialsDto, credentials);
        Credentials updatedCredentials = credentialsRepository.save(credentials);
        return modelMapper.map(updatedCredentials, CredentialsDto.class);
    }

    public void deleteById(Long id) {
        credentialsRepository.deleteById(id);
    }


    public void delete(CredentialsDto credentialsDto) {
        credentialsRepository.delete(modelMapper.map(credentialsDto, Credentials.class));
    }

    public List<CredentialsDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        try {
            Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
            Page<Credentials> credentialsPage = credentialsRepository.findAll(paging);

            return credentialsPage.getContent()
                    .stream()
                    .map(credentials -> modelMapper.map(credentials, CredentialsDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
