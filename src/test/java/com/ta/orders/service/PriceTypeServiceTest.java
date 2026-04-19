package com.ta.orders.service;

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

    @Test
    void shouldCreatePriceType() {
        // Arrange
        PriceType inputPriceType = new PriceType();
        inputPriceType.setName(DEFAULT_PRICE_TYPE_NAME);
        inputPriceType.setDefault(true);

        when(priceTypeRepository.save(inputPriceType)).thenReturn(defaultPriceType);

        // Act
        PriceType result = priceTypeService.create(inputPriceType);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(priceType -> {
                    assertThat(priceType.getId()).isEqualTo(PRICE_TYPE_ID);
                    assertThat(priceType.getName()).isEqualTo(DEFAULT_PRICE_TYPE_NAME);
                    assertThat(priceType.isDefault()).isTrue();
                });

        verify(priceTypeRepository, times(1)).save(inputPriceType);
        verifyNoMoreInteractions(priceTypeRepository);
    }

    @Test
    void shouldReturnSavedPriceTypeWithCorrectData() {
        // Arrange
        PriceType inputPriceType = new PriceType();
        inputPriceType.setName(CLIENT_PRICE_TYPE_NAME);
        inputPriceType.setDefault(false);

        when(priceTypeRepository.save(inputPriceType)).thenReturn(clientPriceType);

        // Act
        PriceType result = priceTypeService.create(inputPriceType);

        // Assert
        assertThat(result)
                .isEqualTo(clientPriceType)
                .hasFieldOrPropertyWithValue("id", CLIENT_PRICE_TYPE_ID)
                .hasFieldOrPropertyWithValue("name", CLIENT_PRICE_TYPE_NAME)
                .hasFieldOrPropertyWithValue("default", false);
    }

    @Test
    void shouldCallRepositorySaveMethodOnce() {
        // Arrange
        PriceType inputPriceType = new PriceType();
        inputPriceType.setName(ANOTHER_PRICE_TYPE_NAME);

        when(priceTypeRepository.save(inputPriceType)).thenReturn(anotherPriceType);

        // Act
        priceTypeService.create(inputPriceType);

        // Assert
        verify(priceTypeRepository, times(1)).save(any(PriceType.class));
    }

    // ========== getAll() tests ==========

    @Test
    void shouldGetAllPriceTypes() {
        // Arrange
        when(priceTypeRepository.findAll()).thenReturn(Arrays.asList(defaultPriceType, clientPriceType, anotherPriceType));

        // Act
        List<PriceType> result = priceTypeService.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(3)
                .containsExactly(defaultPriceType, clientPriceType, anotherPriceType);

        verify(priceTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(priceTypeRepository);
    }

    @Test
    void shouldReturnEmptyListWhenNoPriceTypes() {
        // Arrange
        when(priceTypeRepository.findAll()).thenReturn(List.of());

        // Act
        List<PriceType> result = priceTypeService.getAll();

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

        // Act
        priceTypeService.getAll();

        // Assert
        verify(priceTypeRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnMultiplePriceTypesInCorrectOrder() {
        // Arrange
        when(priceTypeRepository.findAll()).thenReturn(Arrays.asList(defaultPriceType, clientPriceType));

        // Act
        List<PriceType> result = priceTypeService.getAll();

        // Assert
        assertThat(result)
                .hasSize(2)
                .extracting(PriceType::getId)
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

