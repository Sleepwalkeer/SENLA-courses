package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialsService {
    private final CredentialsDao credentialsDao;
    private final ModelMapper modelMapper;

    public CredentialsService(CredentialsDao credentialsDao, ModelMapper modelMapper) {
        this.credentialsDao = credentialsDao;
        this.modelMapper = modelMapper;
    }
    public List<CredentialsDto> getAll() {
        List<CredentialsDto> credentialsDtoList = new ArrayList<>();
        List<Credentials> credentialsList = credentialsDao.getAll();

        for (Credentials credentials : credentialsList) {
            credentialsDtoList.add(fromEntityToDto(credentials));
        }
        return credentialsDtoList;
    }

    public CredentialsDto getById(CredentialsDto credentialsDto) {
        return fromEntityToDto(credentialsDao.getById(fromDtoToEntity(credentialsDto)));
    }

    public CredentialsDto update(CredentialsDto credentialsDto, String newPassword) {
        return fromEntityToDto(credentialsDao.update(fromDtoToEntity(credentialsDto), newPassword));
    }

    public CredentialsDto create(CredentialsDto credentialsDto) {
        return fromEntityToDto(credentialsDao.create(fromDtoToEntity(credentialsDto)));
    }

    public void delete(CredentialsDto credentialsDto) {
        credentialsDao.delete(fromDtoToEntity(credentialsDto));
    }

    private Credentials fromDtoToEntity(CredentialsDto credentialsDto) {
        return modelMapper.map(credentialsDto, Credentials.class);
    }

    private CredentialsDto fromEntityToDto(Credentials credentials) {
        return modelMapper.map(credentials, CredentialsDto.class);
    }
}
