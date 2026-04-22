package com.ta.orders.service;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.mappers.PriceTypeMapper;
import com.ta.orders.model.PriceType;
import com.ta.orders.repository.PriceTypeRepository;
import com.ta.orders.service.impl.PriceTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceTypeServiceTest {

    private static final Long PRICE_TYPE_ID = 1L;
    private static final Long CLIENT_PRICE_TYPE_ID = 2L;
    private static final Long ANOTHER_PRICE_TYPE_ID = 3L;
    private static final Long CLIENT_ID = 100L;
    private static final Long ANOTHER_CLIENT_ID = 200L;
    private static final String DEFAULT_PRICE_TYPE_NAME = "Retail";
    private static final String CLIENT_PRICE_TYPE_NAME = "Wholesale";
    private static final String ANOTHER_PRICE_TYPE_NAME = "VIP";

    @Mock
    private PriceTypeRepository priceTypeRepository;

    @Mock
    private PriceTypeMapper priceTypeMapper;

    @InjectMocks
    private PriceTypeServiceImpl priceTypeService;

    private PriceType defaultPriceType;
    private PriceType clientPriceType;
    private PriceType anotherPriceType;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 4, 19, 10, 30, 0);

        defaultPriceType = new PriceType();
        defaultPriceType.setId(PRICE_TYPE_ID);
        defaultPriceType.setName(DEFAULT_PRICE_TYPE_NAME);
        defaultPriceType.setDefault(true);
        defaultPriceType.setCreationTime(testDateTime);

        clientPriceType = new PriceType();
        clientPriceType.setId(CLIENT_PRICE_TYPE_ID);
        clientPriceType.setName(CLIENT_PRICE_TYPE_NAME);
        clientPriceType.setDefault(false);
        clientPriceType.setCreationTime(testDateTime);

        anotherPriceType = new PriceType();
        anotherPriceType.setId(ANOTHER_PRICE_TYPE_ID);
        anotherPriceType.setName(ANOTHER_PRICE_TYPE_NAME);
        anotherPriceType.setDefault(false);
        anotherPriceType.setCreationTime(testDateTime);
    }

    // ========== create() tests ==========

     // [Old tests removed - replaced with DTO-based tests below]

     // ========== create(PriceTypeDto) tests ==========

     @Test
     void shouldCreatePriceTypeFromDto() {
         // Arrange
         PriceTypeDto inputDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
         PriceTypeDto expectedDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);

         when(priceTypeMapper.toEntity(inputDto)).thenReturn(defaultPriceType);
         when(priceTypeRepository.save(defaultPriceType)).thenReturn(defaultPriceType);
         when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(expectedDto);

         // Act
         PriceTypeDto result = priceTypeService.create(inputDto);

         // Assert
         assertThat(result)
                 .isNotNull()
                 .isEqualTo(expectedDto)
                 .hasFieldOrPropertyWithValue("id", PRICE_TYPE_ID)
                 .hasFieldOrPropertyWithValue("name", DEFAULT_PRICE_TYPE_NAME);

         verify(priceTypeMapper, times(1)).toEntity(inputDto);
         verify(priceTypeRepository, times(1)).save(defaultPriceType);
         verify(priceTypeMapper, times(1)).toDto(defaultPriceType);
     }

     @Test
     void shouldCreateClientPriceTypeDto() {
         // Arrange
         PriceTypeDto inputDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);
         PriceTypeDto expectedDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);

         when(priceTypeMapper.toEntity(inputDto)).thenReturn(clientPriceType);
         when(priceTypeRepository.save(clientPriceType)).thenReturn(clientPriceType);
         when(priceTypeMapper.toDto(clientPriceType)).thenReturn(expectedDto);

         // Act
         PriceTypeDto result = priceTypeService.create(inputDto);

         // Assert
         assertThat(result)
                 .isNotNull()
                 .isEqualTo(expectedDto)
                 .satisfies(dto -> {
                     assertThat(dto.id()).isEqualTo(CLIENT_PRICE_TYPE_ID);
                     assertThat(dto.name()).isEqualTo(CLIENT_PRICE_TYPE_NAME);
                 });

         verify(priceTypeMapper, times(1)).toEntity(inputDto);
         verify(priceTypeRepository, times(1)).save(clientPriceType);
         verify(priceTypeMapper, times(1)).toDto(clientPriceType);
     }

     @Test
     void shouldVerifyMapperAndRepositoryCallSequenceWhenCreatingDto() {
         // Arrange
         PriceTypeDto inputDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
         PriceTypeDto outputDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);

         when(priceTypeMapper.toEntity(inputDto)).thenReturn(defaultPriceType);
         when(priceTypeRepository.save(defaultPriceType)).thenReturn(defaultPriceType);
         when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(outputDto);

         // Act
         PriceTypeDto result = priceTypeService.create(inputDto);

         // Assert
         assertThat(result).isEqualTo(outputDto);
         verify(priceTypeMapper).toEntity(inputDto);
         verify(priceTypeRepository).save(defaultPriceType);
         verify(priceTypeMapper).toDto(defaultPriceType);
     }

     // ========== create(List<PriceTypeDto>) tests ==========

     @Test
     void shouldCreateMultiplePriceTypesFromDtoList() {
         // Arrange
         PriceTypeDto defaultDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
         PriceTypeDto clientDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);
         List<PriceTypeDto> inputDtos = List.of(defaultDto, clientDto);

         when(priceTypeMapper.toEntity(defaultDto)).thenReturn(defaultPriceType);
         when(priceTypeMapper.toEntity(clientDto)).thenReturn(clientPriceType);
         when(priceTypeRepository.save(defaultPriceType)).thenReturn(defaultPriceType);
         when(priceTypeRepository.save(clientPriceType)).thenReturn(clientPriceType);
         when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(defaultDto);
         when(priceTypeMapper.toDto(clientPriceType)).thenReturn(clientDto);

         // Act
         List<PriceTypeDto> result = priceTypeService.create(inputDtos);

         // Assert
         assertThat(result)
                 .isNotNull()
                 .hasSize(2)
                 .containsExactly(defaultDto, clientDto);

         verify(priceTypeMapper, times(2)).toEntity(any(PriceTypeDto.class));
         verify(priceTypeRepository, times(2)).save(any(PriceType.class));
         verify(priceTypeMapper, times(2)).toDto(any(PriceType.class));
     }

     @Test
     void shouldCreateSinglePriceTypeInBatchList() {
         // Arrange
         PriceTypeDto inputDto = new PriceTypeDto(ANOTHER_PRICE_TYPE_ID, ANOTHER_PRICE_TYPE_NAME);
         List<PriceTypeDto> inputDtos = List.of(inputDto);

         when(priceTypeMapper.toEntity(inputDto)).thenReturn(anotherPriceType);
         when(priceTypeRepository.save(anotherPriceType)).thenReturn(anotherPriceType);
         when(priceTypeMapper.toDto(anotherPriceType)).thenReturn(inputDto);

         // Act
         List<PriceTypeDto> result = priceTypeService.create(inputDtos);

         // Assert
         assertThat(result)
                 .isNotNull()
                 .hasSize(1)
                 .contains(inputDto);

         verify(priceTypeMapper, times(1)).toEntity(inputDto);
         verify(priceTypeRepository, times(1)).save(anotherPriceType);
         verify(priceTypeMapper, times(1)).toDto(anotherPriceType);
     }

     @Test
     void shouldCreateEmptyListWhenInputIsEmpty() {
         // Arrange
         List<PriceTypeDto> inputDtos = List.of();

         // Act
         List<PriceTypeDto> result = priceTypeService.create(inputDtos);

         // Assert
         assertThat(result)
                 .isNotNull()
                 .isEmpty();

         verify(priceTypeMapper, never()).toEntity(any(PriceTypeDto.class));
         verify(priceTypeRepository, never()).save(any(PriceType.class));
         verify(priceTypeMapper, never()).toDto(any(PriceType.class));
     }

     @Test
     void shouldCreateThreePriceTypesPreservingOrder() {
         // Arrange
         PriceTypeDto defaultDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
         PriceTypeDto clientDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);
         PriceTypeDto anotherDto = new PriceTypeDto(ANOTHER_PRICE_TYPE_ID, ANOTHER_PRICE_TYPE_NAME);
         List<PriceTypeDto> inputDtos = List.of(defaultDto, clientDto, anotherDto);

         when(priceTypeMapper.toEntity(defaultDto)).thenReturn(defaultPriceType);
         when(priceTypeMapper.toEntity(clientDto)).thenReturn(clientPriceType);
         when(priceTypeMapper.toEntity(anotherDto)).thenReturn(anotherPriceType);
         when(priceTypeRepository.save(defaultPriceType)).thenReturn(defaultPriceType);
         when(priceTypeRepository.save(clientPriceType)).thenReturn(clientPriceType);
         when(priceTypeRepository.save(anotherPriceType)).thenReturn(anotherPriceType);
         when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(defaultDto);
         when(priceTypeMapper.toDto(clientPriceType)).thenReturn(clientDto);
         when(priceTypeMapper.toDto(anotherPriceType)).thenReturn(anotherDto);

         // Act
         List<PriceTypeDto> result = priceTypeService.create(inputDtos);

         // Assert
         assertThat(result)
                 .hasSize(3)
                 .extracting(PriceTypeDto::id)
                 .containsExactly(PRICE_TYPE_ID, CLIENT_PRICE_TYPE_ID, ANOTHER_PRICE_TYPE_ID);

         assertThat(result)
                 .extracting(PriceTypeDto::name)
                 .containsExactly(DEFAULT_PRICE_TYPE_NAME, CLIENT_PRICE_TYPE_NAME, ANOTHER_PRICE_TYPE_NAME);
     }

     // ========== getAll() tests ==========

    @Test
    void shouldGetAllPriceTypes() {
        // Arrange
        PriceTypeDto defaultDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
        PriceTypeDto clientDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);
        PriceTypeDto anotherDto = new PriceTypeDto(ANOTHER_PRICE_TYPE_ID, ANOTHER_PRICE_TYPE_NAME);

        when(priceTypeRepository.findAll()).thenReturn(Arrays.asList(defaultPriceType, clientPriceType, anotherPriceType));
        when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(defaultDto);
        when(priceTypeMapper.toDto(clientPriceType)).thenReturn(clientDto);
        when(priceTypeMapper.toDto(anotherPriceType)).thenReturn(anotherDto);

        // Act
        List<PriceTypeDto> result = priceTypeService.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly(defaultDto, clientDto, anotherDto);

        verify(priceTypeRepository, times(1)).findAll();
        verify(priceTypeMapper, times(3)).toDto(any(PriceType.class));
        verifyNoMoreInteractions(priceTypeRepository);
    }

    @Test
    void shouldReturnEmptyListWhenNoPriceTypes() {
        // Arrange
        when(priceTypeRepository.findAll()).thenReturn(List.of());

        // Act
        List<PriceTypeDto> result = priceTypeService.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(priceTypeRepository, times(1)).findAll();
    }

    @Test
    void shouldCallRepositoryFindAllMethodOnce() {
        // Arrange
        when(priceTypeRepository.findAll()).thenReturn(List.of(defaultPriceType));
        when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME));

        // Act
        priceTypeService.getAll();

        // Assert
        verify(priceTypeRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnMultiplePriceTypesInCorrectOrder() {
        // Arrange
        PriceTypeDto defaultDto = new PriceTypeDto(PRICE_TYPE_ID, DEFAULT_PRICE_TYPE_NAME);
        PriceTypeDto clientDto = new PriceTypeDto(CLIENT_PRICE_TYPE_ID, CLIENT_PRICE_TYPE_NAME);

        when(priceTypeRepository.findAll()).thenReturn(Arrays.asList(defaultPriceType, clientPriceType));
        when(priceTypeMapper.toDto(defaultPriceType)).thenReturn(defaultDto);
        when(priceTypeMapper.toDto(clientPriceType)).thenReturn(clientDto);

        // Act
        List<PriceTypeDto> result = priceTypeService.getAll();

        // Assert
        assertThat(result)
                .hasSize(2)
                .extracting(PriceTypeDto::id)
                .containsExactly(PRICE_TYPE_ID, CLIENT_PRICE_TYPE_ID);
    }

    // ========== getClientsPriceTypeOrDefault() tests ==========

    @Test
    void shouldReturnClientSpecificPriceType() {
        // Arrange
        when(priceTypeRepository.findByClientId(CLIENT_ID)).thenReturn(Optional.of(clientPriceType));

        // Act
        PriceType result = priceTypeService.getClientsPriceTypeOrDefault(CLIENT_ID);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(clientPriceType)
                .hasFieldOrPropertyWithValue("id", CLIENT_PRICE_TYPE_ID)
                .hasFieldOrPropertyWithValue("name", CLIENT_PRICE_TYPE_NAME);

        verify(priceTypeRepository, times(1)).findByClientId(CLIENT_ID);
        verify(priceTypeRepository, never()).findByIsDefaultTrue();
    }

    @Test
    void shouldReturnDefaultPriceTypeWhenClientPriceTypeNotFound() {
        // Arrange
        when(priceTypeRepository.findByClientId(CLIENT_ID)).thenReturn(Optional.empty());
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.of(defaultPriceType));

        // Act
        PriceType result = priceTypeService.getClientsPriceTypeOrDefault(CLIENT_ID);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(defaultPriceType)
                .hasFieldOrPropertyWithValue("id", PRICE_TYPE_ID)
                .hasFieldOrPropertyWithValue("name", DEFAULT_PRICE_TYPE_NAME)
                .hasFieldOrPropertyWithValue("default", true);

        verify(priceTypeRepository, times(1)).findByClientId(CLIENT_ID);
        verify(priceTypeRepository, times(1)).findByIsDefaultTrue();
    }

    @Test
    void shouldReturnNullWhenNeitherClientNorDefaultPriceTypeExists() {
        // Arrange
        when(priceTypeRepository.findByClientId(CLIENT_ID)).thenReturn(Optional.empty());
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.empty());

        // Act
        PriceType result = priceTypeService.getClientsPriceTypeOrDefault(CLIENT_ID);

        // Assert
        assertThat(result).isNull();

        verify(priceTypeRepository, times(1)).findByClientId(CLIENT_ID);
        verify(priceTypeRepository, times(1)).findByIsDefaultTrue();
    }

    @Test
    void shouldCallFindByClientIdBeforeFindingDefault() {
        // Arrange
        when(priceTypeRepository.findByClientId(ANOTHER_CLIENT_ID)).thenReturn(Optional.empty());
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.of(defaultPriceType));

        // Act
        priceTypeService.getClientsPriceTypeOrDefault(ANOTHER_CLIENT_ID);

        // Assert
        verify(priceTypeRepository).findByClientId(ANOTHER_CLIENT_ID);
        verify(priceTypeRepository).findByIsDefaultTrue();
    }

    // ========== getDefaultPriceType() tests ==========

    @Test
    void shouldReturnDefaultPriceTypeWhenExists() {
        // Arrange
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.of(defaultPriceType));

        // Act
        Optional<PriceType> result = priceTypeService.getDefaultPriceType();

        // Assert
        assertThat(result)
                .isPresent()
                .containsSame(defaultPriceType);

        assertThat(result.get())
                .hasFieldOrPropertyWithValue("id", PRICE_TYPE_ID)
                .hasFieldOrPropertyWithValue("name", DEFAULT_PRICE_TYPE_NAME)
                .hasFieldOrPropertyWithValue("default", true);

        verify(priceTypeRepository, times(1)).findByIsDefaultTrue();
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoDefaultPriceType() {
        // Arrange
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.empty());

        // Act
        Optional<PriceType> result = priceTypeService.getDefaultPriceType();

        // Assert
        assertThat(result).isEmpty();

        verify(priceTypeRepository, times(1)).findByIsDefaultTrue();
    }

    @Test
    void shouldCallRepositoryFindByIsDefaultTrueMethodOnce() {
        // Arrange
        when(priceTypeRepository.findByIsDefaultTrue()).thenReturn(Optional.of(defaultPriceType));

        // Act
        priceTypeService.getDefaultPriceType();

        // Assert
        verify(priceTypeRepository, times(1)).findByIsDefaultTrue();
        verifyNoMoreInteractions(priceTypeRepository);
    }
}

