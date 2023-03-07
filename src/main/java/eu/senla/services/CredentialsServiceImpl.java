package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Account;
import eu.senla.entities.Credentials;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsDao credentialsDao;
    private final ModelMapper modelMapper;


    public CredentialsDto getById(Long id) {
        Credentials credentials = credentialsDao.findById(id).orElseThrow(() ->
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
        credentialsDao.save(credentials);
    }

    public CredentialsDto update(Long id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsDao.findById(id).orElseThrow(() ->
                new NotFoundException("No credentials with ID " + id + " was found"));
        modelMapper.map(credentialsDto, credentials);
        Credentials updatedCredentials = credentialsDao.save(credentials);
        return modelMapper.map(updatedCredentials, CredentialsDto.class);
    }

    public void deleteById(Long id) {
        credentialsDao.deleteById(id);
    }


    public void delete(CredentialsDto credentialsDto) {
        credentialsDao.delete(modelMapper.map(credentialsDto, Credentials.class));
    }

    public List<CredentialsDto> getAll() {
        try {
            List<Credentials> credentialss = credentialsDao.findAll();
            return credentialss.stream()
                    .map(credentials -> modelMapper.map(credentials, CredentialsDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseAccessException("Unable to access database");
        }
    }
}
