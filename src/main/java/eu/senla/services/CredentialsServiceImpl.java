package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Credentials;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsDao credentialsDao;
    private final ModelMapper modelMapper;


    public List<CredentialsDto> getAll() {
        List<CredentialsDto> credentialsDtoList = new ArrayList<>();
        List<Credentials> credentialsList = credentialsDao.findAll();

        for (Credentials credentials : credentialsList) {
            credentialsDtoList.add(modelMapper.map(credentials, CredentialsDto.class));
        }
        return credentialsDtoList;
    }

    public CredentialsDto getById(Integer id) {
        return modelMapper.map(credentialsDao.findById(id), CredentialsDto.class);
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
