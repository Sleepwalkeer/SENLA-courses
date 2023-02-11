package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Credentials;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsDao credentialsDao;
    private final ModelMapper modelMapper;

    public CredentialsServiceImpl(CredentialsDao credentialsDao, ModelMapper modelMapper) {
        this.credentialsDao = credentialsDao;
        this.modelMapper = modelMapper;
    }

    public List<CredentialsDto> getAll() {
        List<CredentialsDto> credentialsDtoList = new ArrayList<>();
        List<Credentials> credentialsList = credentialsDao.findAll();

        for (Credentials credentials : credentialsList) {
            credentialsDtoList.add(modelMapper.map(credentials, CredentialsDto.class));
        }
        return credentialsDtoList;
    }

    public CredentialsDto getById(CredentialsDto credentialsDto) {
        return modelMapper.map(credentialsDao.findById(credentialsDto.getId()), CredentialsDto.class);
    }

    public void create(CredentialsDto credentialsDto) {
        credentialsDao.save(modelMapper.map(credentialsDto, Credentials.class));
    }

    public CredentialsDto update(CredentialsDto credentialsDto) {
        return modelMapper.map(credentialsDao.update(modelMapper.map(credentialsDto, Credentials.class)), CredentialsDto.class);
    }


    public void delete(CredentialsDto credentialsDto) {
        credentialsDao.delete(modelMapper.map(credentialsDto, Credentials.class));
    }
}
