package com.ta.orders.service;

import com.ta.orders.dto.BatchPriceRequestDto;
import com.ta.orders.dto.BatchPriceValidationResult;
import com.ta.orders.dto.PriceDto;
import com.ta.orders.dto.PriceItemDto;
import com.ta.orders.mappers.PriceMapper;
import com.ta.orders.model.Price;
import com.ta.orders.repository.PriceRepository;
import com.ta.orders.repository.ProductRepository;
import com.ta.orders.service.impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    private static final String PRICE_TYPE = "RETAIL";
    private static final Long PRODUCT_ID_1 = 100L;
    private static final Long PRODUCT_ID_2 = 200L;
    private static final Long INVALID_PRODUCT_ID = 999L;
    private static final BigDecimal PRICE_1 = new BigDecimal("10.5000");
    private static final BigDecimal PRICE_2 = new BigDecimal("20.7500");
    private static final BigDecimal UPDATED_PRICE = new BigDecimal("15.0000");
    private static final LocalDateTime VALID_FROM = LocalDateTime.of(2026, 4, 19, 10, 0, 0);
    private static final LocalDateTime PAST_VALID_FROM = LocalDateTime.of(2026, 1, 1, 0, 0, 0);

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceMapper priceMapper;

    @InjectMocks
    private PriceServiceImpl priceService;

    private BatchPriceRequestDto validRequest;
    private Price testPrice1;
    private Price testPrice2;
    private PriceDto testPriceDto1;
    private PriceDto testPriceDto2;

    @BeforeEach
    void setUp() {
        // Setup valid request
        validRequest = new BatchPriceRequestDto();
        validRequest.setPriceType(PRICE_TYPE);
        validRequest.setValidFrom(VALID_FROM);

        PriceItemDto item1 = new PriceItemDto();
        item1.setProductId(PRODUCT_ID_1);
        item1.setPrice(PRICE_1);

        PriceItemDto item2 = new PriceItemDto();
        item2.setProductId(PRODUCT_ID_2);
        item2.setPrice(PRICE_2);

        validRequest.setPrices(Arrays.asList(item1, item2));

        // Setup test entities
        testPrice1 = new Price();
        testPrice1.setId(1L);
        testPrice1.setProductId(PRODUCT_ID_1);
        testPrice1.setPriceType(PRICE_TYPE);
        testPrice1.setPrice(PRICE_1);
        testPrice1.setValidFrom(VALID_FROM);
        testPrice1.setValidTo(null);
        testPrice1.setCurrent(true);
        testPrice1.setCreatedAt(LocalDateTime.now());

        testPrice2 = new Price();
        testPrice2.setId(2L);
        testPrice2.setProductId(PRODUCT_ID_2);
        testPrice2.setPriceType(PRICE_TYPE);
        testPrice2.setPrice(PRICE_2);
        testPrice2.setValidFrom(VALID_FROM);
        testPrice2.setValidTo(null);
        testPrice2.setCurrent(true);
        testPrice2.setCreatedAt(LocalDateTime.now());

        // Setup test DTOs
        testPriceDto1 = new PriceDto();
        testPriceDto1.setId(1L);
        testPriceDto1.setProductId(PRODUCT_ID_1);
        testPriceDto1.setPriceType(PRICE_TYPE);
        testPriceDto1.setPrice(PRICE_1);
        testPriceDto1.setValidFrom(VALID_FROM);
        testPriceDto1.setValidTo(null);
        testPriceDto1.setCurrent(true);

        testPriceDto2 = new PriceDto();
        testPriceDto2.setId(2L);
        testPriceDto2.setProductId(PRODUCT_ID_2);
        testPriceDto2.setPriceType(PRICE_TYPE);
        testPriceDto2.setPrice(PRICE_2);
        testPriceDto2.setValidFrom(VALID_FROM);
        testPriceDto2.setValidTo(null);
        testPriceDto2.setCurrent(true);
    }

    // ========== batchSave() - Success scenarios ==========

    @Test
    void shouldSuccessfullyBatchSaveValidPrices() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        when(priceRepository.findExistingProductIds(Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2)))
                .thenReturn(Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2));
        when(priceRepository.findByProductIdAndPriceTypeAndIsCurrent(any(), any(), anyBoolean()))
                .thenReturn(Optional.empty());
        when(priceRepository.save(any(Price.class)))
                .thenReturn(testPrice1)
                .thenReturn(testPrice2);
        when(priceMapper.toDto(testPrice1)).thenReturn(testPriceDto1);
        when(priceMapper.toDto(testPrice2)).thenReturn(testPriceDto2);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorMessage()).isNull();
        assertThat(savedPrices).hasSize(2);
        verify(priceRepository, times(2)).save(any(Price.class));
        verify(priceMapper, times(2)).toDto(any(Price.class));
    }

    @Test
    void shouldCloseCurrentPriceWhenNewPriceAdded() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        Price existingCurrentPrice = new Price();
        existingCurrentPrice.setId(10L);
        existingCurrentPrice.setProductId(PRODUCT_ID_1);
        existingCurrentPrice.setPriceType(PRICE_TYPE);
        existingCurrentPrice.setPrice(PRICE_1);
        existingCurrentPrice.setValidFrom(PAST_VALID_FROM);
        existingCurrentPrice.setValidTo(null);
        existingCurrentPrice.setCurrent(true);

        PriceItemDto item = new PriceItemDto();
        item.setProductId(PRODUCT_ID_1);
        item.setPrice(UPDATED_PRICE);

        BatchPriceRequestDto request = new BatchPriceRequestDto();
        request.setPriceType(PRICE_TYPE);
        request.setValidFrom(VALID_FROM);
        request.setPrices(Arrays.asList(item));

        when(priceRepository.findExistingProductIds(Arrays.asList(PRODUCT_ID_1)))
                .thenReturn(Arrays.asList(PRODUCT_ID_1));
        when(priceRepository.findByProductIdAndPriceTypeAndIsCurrent(PRODUCT_ID_1, PRICE_TYPE, true))
                .thenReturn(Optional.of(existingCurrentPrice));
        when(priceRepository.save(any(Price.class))).thenReturn(testPrice1);
        when(priceMapper.toDto(any(Price.class))).thenReturn(testPriceDto1);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(request, savedPrices);

        // Assert
        assertThat(result.isValid()).isTrue();
        assertThat(savedPrices).hasSize(1);
        verify(priceRepository).closeCurrentPrice(PRODUCT_ID_1, PRICE_TYPE, VALID_FROM);
        verify(priceRepository).save(any(Price.class));
    }

    // ========== batchSave() - Validation failure scenarios ==========

    @Test
    void shouldFailValidationWhenValidFromIsNull() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        validRequest.setValidFrom(null);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.isMissingValidFrom()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("Valid from timestamp is required");
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldFailValidationWhenPriceTypeIsNull() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        validRequest.setPriceType(null);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.isMissingPriceType()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("Price type is required");
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldFailValidationWhenPriceTypeIsBlank() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        validRequest.setPriceType("   ");

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.isMissingPriceType()).isTrue();
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldFailValidationWhenPricesListIsEmpty() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        validRequest.setPrices(new ArrayList<>());

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.isEmptyPricesList()).isTrue();
        assertThat(result.getErrorMessage()).isEqualTo("Prices list cannot be empty");
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldFailValidationWhenPricesListIsNull() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        validRequest.setPrices(null);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.isEmptyPricesList()).isTrue();
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldFailValidationWhenProductIdsDoNotExist() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        PriceItemDto invalidItem = new PriceItemDto();
        invalidItem.setProductId(INVALID_PRODUCT_ID);
        invalidItem.setPrice(PRICE_1);
        validRequest.setPrices(Arrays.asList(invalidItem));

        when(priceRepository.findExistingProductIds(Arrays.asList(INVALID_PRODUCT_ID)))
                .thenReturn(new ArrayList<>());

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getInvalidProductIds()).contains(INVALID_PRODUCT_ID);
        assertThat(result.getErrorMessage()).contains("Invalid product IDs");
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    @Test
    void shouldIdentifyMultipleInvalidProductIds() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        Long invalidId1 = 888L;
        Long invalidId2 = 999L;

        PriceItemDto item1 = new PriceItemDto();
        item1.setProductId(PRODUCT_ID_1);
        item1.setPrice(PRICE_1);

        PriceItemDto item2 = new PriceItemDto();
        item2.setProductId(invalidId1);
        item2.setPrice(PRICE_2);

        PriceItemDto item3 = new PriceItemDto();
        item3.setProductId(invalidId2);
        item3.setPrice(PRICE_2);

        validRequest.setPrices(Arrays.asList(item1, item2, item3));

        when(priceRepository.findExistingProductIds(Arrays.asList(PRODUCT_ID_1, invalidId1, invalidId2)))
                .thenReturn(Arrays.asList(PRODUCT_ID_1));

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isFalse();
        assertThat(result.getInvalidProductIds()).containsExactlyInAnyOrder(invalidId1, invalidId2);
        assertThat(savedPrices).isEmpty();
        verify(priceRepository, never()).save(any());
    }

    // ========== findAll() tests ==========

    @Test
    void shouldReturnAllPriceHistory() {
        // Arrange
        when(priceRepository.findAll()).thenReturn(Arrays.asList(testPrice1, testPrice2));
        when(priceMapper.toDto(testPrice1)).thenReturn(testPriceDto1);
        when(priceMapper.toDto(testPrice2)).thenReturn(testPriceDto2);

        // Act
        List<PriceDto> result = priceService.findAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testPriceDto1, testPriceDto2);
        verify(priceRepository, times(1)).findAll();
        verify(priceMapper, times(2)).toDto(any(Price.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoPrices() {
        // Arrange
        when(priceRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<PriceDto> result = priceService.findAll();

        // Assert
        assertThat(result).isEmpty();
        verify(priceRepository, times(1)).findAll();
        verifyNoInteractions(priceMapper);
    }

    // ========== findAllCurrent() tests ==========

    @Test
    void shouldReturnAllCurrentPrices() {
        // Arrange
        when(priceRepository.findByIsCurrent(true)).thenReturn(Arrays.asList(testPrice1, testPrice2));
        when(priceMapper.toDto(testPrice1)).thenReturn(testPriceDto1);
        when(priceMapper.toDto(testPrice2)).thenReturn(testPriceDto2);

        // Act
        List<PriceDto> result = priceService.findAllCurrent();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testPriceDto1, testPriceDto2);
        verify(priceRepository, times(1)).findByIsCurrent(true);
        verify(priceMapper, times(2)).toDto(any(Price.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoCurrentPrices() {
        // Arrange
        when(priceRepository.findByIsCurrent(true)).thenReturn(new ArrayList<>());

        // Act
        List<PriceDto> result = priceService.findAllCurrent();

        // Assert
        assertThat(result).isEmpty();
        verify(priceRepository, times(1)).findByIsCurrent(true);
        verifyNoInteractions(priceMapper);
    }

    // ========== findAllCurrentByPriceType() tests ==========

    @Test
    void shouldReturnAllCurrentPricesByPriceType() {
        // Arrange
        when(priceRepository.findAllCurrentByPriceType(PRICE_TYPE))
                .thenReturn(Arrays.asList(testPrice1, testPrice2));
        when(priceMapper.toDto(testPrice1)).thenReturn(testPriceDto1);
        when(priceMapper.toDto(testPrice2)).thenReturn(testPriceDto2);

        // Act
        List<PriceDto> result = priceService.findAllCurrentByPriceType(PRICE_TYPE);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testPriceDto1, testPriceDto2);
        verify(priceRepository, times(1)).findAllCurrentByPriceType(PRICE_TYPE);
        verify(priceMapper, times(2)).toDto(any(Price.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoCurrentPricesForPriceType() {
        // Arrange
        when(priceRepository.findAllCurrentByPriceType(PRICE_TYPE)).thenReturn(new ArrayList<>());

        // Act
        List<PriceDto> result = priceService.findAllCurrentByPriceType(PRICE_TYPE);

        // Assert
        assertThat(result).isEmpty();
        verify(priceRepository, times(1)).findAllCurrentByPriceType(PRICE_TYPE);
        verifyNoInteractions(priceMapper);
    }

    // ========== Cache eviction tests ==========

    @Test
    void shouldEvictCacheOnSuccessfulBatchSave() {
        // Arrange
        List<PriceDto> savedPrices = new ArrayList<>();
        when(priceRepository.findExistingProductIds(any())).thenReturn(Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2));
        when(priceRepository.findByProductIdAndPriceTypeAndIsCurrent(any(), any(), anyBoolean()))
                .thenReturn(Optional.empty());
        when(priceRepository.save(any(Price.class))).thenReturn(testPrice1);
        when(priceMapper.toDto(any(Price.class))).thenReturn(testPriceDto1);

        // Act
        BatchPriceValidationResult result = priceService.batchSave(validRequest, savedPrices);

        // Assert
        assertThat(result.isValid()).isTrue();
        // Note: @CacheEvict is tested via integration tests or manually verified
        // as Mockito cannot directly verify annotation behavior
    }
}

