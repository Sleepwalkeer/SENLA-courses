package eu.senla.service;

import eu.senla.dto.credentialsDto.CredentialsDto;
import eu.senla.dto.credentialsDto.ResponseCredentialsDto;
import eu.senla.entity.Credentials;
import eu.senla.exception.NotFoundException;
import eu.senla.repository.CredentialsRepository;
import eu.senla.service.implementation.CredentialsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CredentialsServiceTest {
    @Mock
    private CredentialsRepository credentialsRepository;
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
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        credentialsService.create(credentialsDto);

        verify(credentialsRepository).save(credentials);
    }

    @Test
    public void getByIdTest() {
        Credentials credentials = Credentials.builder().username("test").build();
        ResponseCredentialsDto credentialsDto = ResponseCredentialsDto.builder().username("test").build();

        when(credentialsRepository.findById(1L)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, ResponseCredentialsDto.class)).thenReturn(credentialsDto);

        ResponseCredentialsDto credentialsDtoRetrieved = credentialsService.getById(1L);

        verify(credentialsRepository).findById(1L);
        Assertions.assertNotNull(credentialsDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(credentialsRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.getById(1L));
    }

    @Test
    public void updateTest() {
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(credentialsRepository.findById(1L)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        credentialsService.update(1L, credentialsDto);

        verify(credentialsRepository).findById(1L);
        verify(credentialsRepository).save(credentials);
    }

    @Test
    public void updateNonExistentCredentialsTest() {
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(credentialsRepository.findById(1L)).thenReturn(Optional.empty());
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.update(1L, credentialsDto));
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(credentialsRepository).deleteById(1L);
        credentialsService.deleteById(1L);
        verify(credentialsRepository).deleteById(1L);
    }
}
