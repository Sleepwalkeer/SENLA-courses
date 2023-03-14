package eu.senla.service;

import eu.senla.entity.Order;
import eu.senla.repository.CredentialsRepository;
import eu.senla.dto.CredentialsDto;
import eu.senla.entity.Credentials;
import eu.senla.exception.BadRequestException;
import eu.senla.exception.DatabaseAccessException;
import eu.senla.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
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
    public void createWithInvalidDataTest() {
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).build();
        Assertions.assertThrows(BadRequestException.class, () -> credentialsService.create(credentialsDto));
    }

    @Test
    public void getByIdTest() {
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();

        when(credentialsRepository.findById(1L)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);

        CredentialsDto credentialsDtoRetrieved = credentialsService.getById(1L);

        verify(credentialsRepository).findById(1L);
        Assertions.assertNotNull(credentialsDto);
        Assertions.assertEquals(credentialsDto, credentialsDtoRetrieved);
    }

    @Test
    public void getByInvalidIdTest() {
        when(credentialsRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> credentialsService.getById(1L));
        verify(credentialsRepository).findById(1L);
    }

    @Test
    public void updateTest() {
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();

        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(credentialsRepository.findById(1L)).thenReturn(Optional.ofNullable(credentials));
        when(modelMapper.map(credentials, CredentialsDto.class)).thenReturn(credentialsDto);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);

        CredentialsDto credentialsDtoRetrieved = credentialsService.update(1L, credentialsDto);

        verify(credentialsRepository).findById(1L);
        verify(credentialsRepository).save(credentials);
        Assertions.assertEquals(credentialsDto, credentialsDtoRetrieved);
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
        verify(credentialsRepository).findById(1L);
    }


    @Test
    public void deleteTest() {
        Credentials credentials = Credentials.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto = CredentialsDto.builder().id(1L).password("test").username("test").build();
        doNothing().when(credentialsRepository).delete(credentials);
        when(modelMapper.map(credentialsDto, Credentials.class)).thenReturn(credentials);
        credentialsService.delete(credentialsDto);
        verify(credentialsRepository).delete(credentials);
    }


    @Test
    public void deleteByIdTest() {
        doNothing().when(credentialsRepository).deleteById(1L);
        credentialsService.deleteById(1L);
        verify(credentialsRepository).deleteById(1L);
    }

    @Test
    public void getAllTest() {
        CredentialsDto credentialsDto1 = CredentialsDto.builder().id(1L).password("test").username("test").build();
        CredentialsDto credentialsDto2 = CredentialsDto.builder().id(2L).password("tost").username("tost").build();
        List<CredentialsDto> credentialsDtos = new ArrayList<>();
        credentialsDtos.add(credentialsDto1);
        credentialsDtos.add(credentialsDto2);

        Credentials credentials1 = Credentials.builder().id(1L).password("test").username("test").build();
        Credentials credentials2 = Credentials.builder().id(2L).password("tost").username("tost").build();
        List<Credentials> credentialsList = new ArrayList<>();
        credentialsList.add(credentials1);
        credentialsList.add(credentials2);

        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        Page<Credentials> credentialsPage = new PageImpl<>(credentialsList,paging,credentialsList.size());

        when(credentialsRepository.findAll(paging)).thenReturn(credentialsPage);
        when(modelMapper.map(eq(credentials1), eq(CredentialsDto.class)))
                .thenReturn(credentialsDto1);
        when(modelMapper.map(eq(credentials2), eq(CredentialsDto.class)))
                .thenReturn(credentialsDto2);

        List<CredentialsDto> retrievedCredentialsDtos = credentialsService.getAll(1,2,"id");

        verify(credentialsRepository).findAll(paging);
        Assertions.assertIterableEquals(credentialsDtos, retrievedCredentialsDtos);
    }

    @Test
    public void getAllWithDatabaseAccessExceptionTest() {
        Pageable paging = PageRequest.of(1, 2, Sort.by("id"));
        when(credentialsRepository.findAll(paging)).thenThrow(new RuntimeException());
        Assertions.assertThrows(DatabaseAccessException.class, () -> credentialsService.getAll(1,2,"id"));
        verify(credentialsRepository).findAll(paging);
    }
}
