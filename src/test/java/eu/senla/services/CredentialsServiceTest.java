package eu.senla.services;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.CredentialsDto;
import eu.senla.dto.CredentialsDto;
import eu.senla.entities.Credentials;
import eu.senla.entities.Credentials;
import eu.senla.exceptions.BadRequestException;
import eu.senla.exceptions.DatabaseAccessException;
import eu.senla.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CredentialsServiceTest {
    @Mock
    private CredentialsDao credentialsDao;

    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private CredentialsServiceImpl credentialsService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        doNothing().when(credentialsDao).save(credentials);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        credentialsService.create(credentialsDto);

        verify(credentialsDao).save(credentials);
    }

    @Test
    public void createWithInvalidDataTest() {
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).build();
        Assertions.assertThrows(BadRequestException.class, () -> credentialsService.create(credentialsDto));
    }

    @Test
    public void getByIdTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        when(credentialsDao.findById(1)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);

        CredentialsDto credentialsDtoRetrieved = credentialsService.getById(1);

        verify(credentialsDao).findById(1);
        Assertions.assertNotNull(credentialsDto);
        Assertions.assertEquals(credentialsDto, credentialsDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(credentialsDao.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.getById(1));
        verify(credentialsDao).findById(1);
    }

    @Test
    public void updateTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        when(credentialsDao.update(credentials)).thenReturn(credentials);
        when(credentialsDao.findById(1)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        CredentialsDto credentialsDtoRetrieved = credentialsService.update(1, credentialsDto);


        verify(credentialsDao).findById(1);
        verify(credentialsDao).update(credentials);
        Assertions.assertEquals(credentialsDto, credentialsDtoRetrieved);
    }

    @Test
    public void updateNonExistentCredentialsTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        when(credentialsDao.update(credentials)).thenReturn(credentials);
        when(credentialsDao.findById(1)).thenReturn(Optional.empty());
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.update(1, credentialsDto));
        verify(credentialsDao).findById(1);
    }


    @Test
    public void deleteTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        when(credentialsDao.delete(credentials)).thenReturn(true);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        Assertions.assertTrue(credentialsService.delete(credentialsDto));
        verify(credentialsDao).delete(credentials);
    }

    @Test
    public void deleteNonExistentCredentialsTest() {
        Credentials credentials = Credentials.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1).password("test").username("test").build();

        when(credentialsDao.delete(credentials)).thenReturn(false);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        Assertions.assertFalse(credentialsService.delete(credentialsDto));
        verify(credentialsDao).delete(credentials);
    }

    @Test
    public void deleteByIdTest() {
        when(credentialsDao.deleteById(1)).thenReturn(true);

        Assertions.assertTrue(credentialsService.deleteById(1));
        verify(credentialsDao).deleteById(1);
    }

    @Test
    public void deleteByNonExistentIdTest() {
        when(credentialsDao.deleteById(1)).thenReturn(false);

        Assertions.assertFalse(credentialsService.deleteById(1));
        verify(credentialsDao).deleteById(1);
    }

    @Test
    public void getAllTest() {
        CredentialsDto credentialsDto1 = CredentialsDto.builder().id(1).password("test").username("test").build();
        CredentialsDto credentialsDto2 = CredentialsDto.builder().id(2).password("tost").username("tost").build();
        List<CredentialsDto> credentialsDtos = new ArrayList<>();
        credentialsDtos.add(credentialsDto1);
        credentialsDtos.add(credentialsDto2);

        Credentials credentials1 = Credentials.builder().id(1).password("test").username("test").build();
        Credentials credentials2 = Credentials.builder().id(2).password("tost").username("tost").build();
        List<Credentials> credentialsList = new ArrayList<>();
        credentialsList.add(credentials1);
        credentialsList.add(credentials2);

        when(credentialsDao.findAll()).thenReturn(credentialsList);
        when(modelMapper.map(eq(credentials1), eq(CredentialsDto.class)))
                .thenReturn(credentialsDto1);
        when(modelMapper.map(eq(credentials2), eq(CredentialsDto.class)))
                .thenReturn(credentialsDto2);

        List<CredentialsDto> retrievedCredentialsDtos = credentialsService.getAll();

        verify(credentialsDao).findAll();
        Assertions.assertIterableEquals(credentialsDtos, retrievedCredentialsDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        when(credentialsDao.findAll()).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> credentialsService.getAll());
        verify(credentialsDao).findAll();
    }
}
