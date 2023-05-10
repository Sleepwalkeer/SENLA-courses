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
        Credentials credentials = Credentials.builder().id(1L).password("RentalApplication").username("RentalApplication").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("RentalApplication").username("RentalApplication").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        credentialsService.create(credentialsDto);

        verify(credentialsRepository).save(credentials);
    }

    @Test
    public void getByIdTest() {
        Credentials credentials = Credentials.builder().username("RentalApplication").build();
        ResponseCredentialsDto credentialsDto = ResponseCredentialsDto.builder().username("RentalApplication").build();

        when(credentialsRepository.findById(1L)).thenReturn(Optional.of(credentials));
        when(modelMapper.map(credentials, ResponseCredentialsDto.class)).thenReturn(credentialsDto);

        ResponseCredentialsDto credentialsDtoRetrieved = credentialsService.getById(1L);

        verify(credentialsRepository).findById(1L);
        Assertions.assertNotNull(credentialsDtoRetrieved);
    }

    @Test
    public void getByNonexistentIdTest() {
        when(credentialsRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.getById(1L));
    }

    @Test
    public void updateTest() {
        Credentials credentials = Credentials.builder().id(1L).password("RentalApplication").username("RentalApplication").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("RentalApplication").username("RentalApplication").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(credentialsRepository.existsById(1L)).thenReturn(true);
        when(credentialsRepository.findById(1L)).thenReturn(Optional.of(credentials));

        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        credentialsService.update(1L, credentialsDto);

        verify(credentialsRepository).existsById(1L);
        verify(credentialsRepository).save(credentials);
    }

    @Test
    public void updateNonexistentCredentialsTest() {
        Credentials credentials = Credentials.builder().id(1L).password("RentalApplication").username("RentalApplication").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("RentalApplication").username("RentalApplication").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(credentialsRepository.existsById(1L)).thenReturn(false);

        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        verify(credentialsRepository, times(0)).save(credentials);
        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.update(1L, credentialsDto));
    }

    @Test
    public void deleteByIdTest() {
        doNothing().when(credentialsRepository).deleteById(1L);
        when(credentialsRepository.existsById(1L)).thenReturn(true);
        credentialsService.deleteById(1L);

        verify(credentialsRepository).deleteById(1L);
    }

    @Test
    public void deleteByNonexistentIdTest() {
        doNothing().when(credentialsRepository).deleteById(1L);
        when(credentialsRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.deleteById(1L));
        verify(credentialsRepository, times(0)).deleteById(1L);
    }
}
