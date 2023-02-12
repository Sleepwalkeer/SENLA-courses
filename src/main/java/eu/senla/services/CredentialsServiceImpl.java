package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Credentials;
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


    public CredentialsDto getById(Integer id) {
        Credentials credentials = credentialsDao.findById(id).orElse(null);
        if (credentials == null) {
            return null;
        }
        return modelMapper.map(credentials, CredentialsDto.class);
    }

    public void create(CredentialsDto credentialsDto) {
        Credentials credentials = modelMapper.map(credentialsDto, Credentials.class);
        credentialsDao.save(credentials);
    }

    public CredentialsDto update(Integer id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsDao.findById(id).orElse(null);
        if (credentials == null) {
            return null;
        }
        modelMapper.map(credentialsDto, credentials);
        Credentials updatedCredentials = credentialsDao.update(credentials);
        return modelMapper.map(updatedCredentials, CredentialsDto.class);
    }

    public boolean deleteById(Integer id) {
        return credentialsDao.deleteById(id);
    }

    @Override
    public boolean delete(CredentialsDto credentialsDto) {
        return credentialsDao.delete(modelMapper.map(credentialsDto, Credentials.class));
    }

    public List<CredentialsDto> getAll() {
        List<Credentials> credentialss = credentialsDao.findAll();
        return credentialss.stream()
                .map(credentials -> modelMapper.map(credentials, CredentialsDto.class))
                .collect(Collectors.toList());
    }
}
