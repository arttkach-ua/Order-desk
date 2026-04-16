package com.ta.orders.service;

import com.ta.orders.dto.ProductCategoryDto;
import com.ta.orders.mappers.ProductCategoryMapper;
import com.ta.orders.model.ProductCategory;
import com.ta.orders.repository.ProductCategoryRepository;
import com.ta.orders.service.impl.ProductCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    public static final String BEVERAGES = "Beverages";
    public static final String PIZZA = "Pizza";
    public static final String PIZZA_URL = "https://example.com/pizza.jpg";
    public static final String BEVERAGES_JPG = "https://example.com/beverages.jpg";
    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    private ProductCategoryDto testDto;
    private ProductCategory testEntity;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 4, 16, 10, 30, 0);

        testDto = new ProductCategoryDto();
        testDto.setId(1L);
        testDto.setName(BEVERAGES);
        testDto.setImageUrl(BEVERAGES_JPG);

        testEntity = new ProductCategory();
        testEntity.setId(1L);
        testEntity.setName(BEVERAGES);
        testEntity.setImageUrl(BEVERAGES_JPG);
        testEntity.setCreationTime(testDateTime);
    }

    @Test
    void shouldCreateProductCategory() {
        // Arrange
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName(BEVERAGES);
        inputDto.setImageUrl(BEVERAGES_JPG);

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(testEntity);
        when(productCategoryRepository.save(testEntity)).thenReturn(testEntity);
        when(productCategoryMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        ProductCategoryDto result = productCategoryService.create(inputDto);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(1L);
                    assertThat(dto.getName()).isEqualTo(BEVERAGES);
                    assertThat(dto.getImageUrl()).isEqualTo(BEVERAGES_JPG);
                });

        verify(productCategoryMapper, times(1)).toEntity(inputDto);
        verify(productCategoryRepository, times(1)).save(testEntity);
        verify(productCategoryMapper, times(1)).toDto(testEntity);
        verifyNoMoreInteractions(productCategoryRepository, productCategoryMapper);
    }

    @Test
    void shouldReturnProductCategoryDtoWithCorrectData() {
        // Arrange
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName(PIZZA);
        inputDto.setImageUrl(PIZZA_URL);

        ProductCategory savedEntity = new ProductCategory();
        savedEntity.setId(2L);
        savedEntity.setName(PIZZA);
        savedEntity.setImageUrl(PIZZA_URL);
        savedEntity.setCreationTime(testDateTime);

        ProductCategoryDto expectedDto = new ProductCategoryDto();
        expectedDto.setId(2L);
        expectedDto.setName(PIZZA);
        expectedDto.setImageUrl(PIZZA_URL);

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(savedEntity);
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(savedEntity);
        when(productCategoryMapper.toDto(savedEntity)).thenReturn(expectedDto);

        // Act
        ProductCategoryDto result = productCategoryService.create(inputDto);

        // Assert
        assertThat(result)
                .isEqualTo(expectedDto)
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", PIZZA)
                .hasFieldOrPropertyWithValue("imageUrl", PIZZA_URL);
    }

    @Test
    void shouldCallRepositorySaveMethodOnce() {
        // Arrange
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName("Desserts");

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(testEntity);
        when(productCategoryRepository.save(testEntity)).thenReturn(testEntity);
        when(productCategoryMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        productCategoryService.create(inputDto);

        // Assert
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @Test
    void shouldGetAllProductCategories() {
        // Arrange
        ProductCategory category1 = new ProductCategory();
        category1.setId(1L);
        category1.setName(BEVERAGES);
        category1.setImageUrl(BEVERAGES_JPG);

        ProductCategory category2 = new ProductCategory();
        category2.setId(2L);
        category2.setName(PIZZA);
        category2.setImageUrl(PIZZA_URL);

        ProductCategoryDto dto1 = new ProductCategoryDto();
        dto1.setId(1L);
        dto1.setName(BEVERAGES);
        dto1.setImageUrl(BEVERAGES_JPG);

        ProductCategoryDto dto2 = new ProductCategoryDto();
        dto2.setId(2L);
        dto2.setName(PIZZA);
        dto2.setImageUrl(PIZZA_URL);

        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        when(productCategoryMapper.toDto(category1)).thenReturn(dto1);
        when(productCategoryMapper.toDto(category2)).thenReturn(dto2);

        // Act
        List<ProductCategoryDto> result = productCategoryService.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(dto1, dto2);

        verify(productCategoryRepository, times(1)).findAll();
        verify(productCategoryMapper, times(2)).toDto(any(ProductCategory.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoProductCategories() {
        // Arrange
        when(productCategoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductCategoryDto> result = productCategoryService.getAll();

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(productCategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(productCategoryMapper);
    }

    @Test
    void shouldCallRepositoryFindAllMethodOnce() {
        // Arrange
        when(productCategoryRepository.findAll()).thenReturn(List.of(testEntity));
        when(productCategoryMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        productCategoryService.getAll();

        // Assert
        verify(productCategoryRepository, times(1)).findAll();
    }

    @Test
    void shouldMapAllEntitiesToDtos() {
        // Arrange
        ProductCategory category1 = new ProductCategory();
        category1.setId(1L);
        category1.setName("Category1");

        ProductCategory category2 = new ProductCategory();
        category2.setId(2L);
        category2.setName("Category2");

        ProductCategoryDto dto1 = new ProductCategoryDto();
        dto1.setId(1L);
        dto1.setName("Category1");

        ProductCategoryDto dto2 = new ProductCategoryDto();
        dto2.setId(2L);
        dto2.setName("Category2");

        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        when(productCategoryMapper.toDto(category1)).thenReturn(dto1);
        when(productCategoryMapper.toDto(category2)).thenReturn(dto2);

        // Act
        List<ProductCategoryDto> result = productCategoryService.getAll();

        // Assert
        assertThat(result).hasSize(2);
        verify(productCategoryMapper).toDto(category1);
        verify(productCategoryMapper).toDto(category2);
    }

    @Test
    void shouldPreserveCategoryNameWhenCreating() {
        // Arrange
        String categoryName = "Grilled Items";
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName(categoryName);

        ProductCategory entityToSave = new ProductCategory();
        entityToSave.setName(categoryName);

        ProductCategory savedEntity = new ProductCategory();
        savedEntity.setId(5L);
        savedEntity.setName(categoryName);

        ProductCategoryDto resultDto = new ProductCategoryDto();
        resultDto.setId(5L);
        resultDto.setName(categoryName);

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(entityToSave);
        when(productCategoryRepository.save(entityToSave)).thenReturn(savedEntity);
        when(productCategoryMapper.toDto(savedEntity)).thenReturn(resultDto);

        // Act
        ProductCategoryDto result = productCategoryService.create(inputDto);

        // Assert
        assertThat(result.getName()).isEqualTo(categoryName);
    }

    @Test
    void shouldPreserveCategoryImageUrlWhenCreating() {
        // Arrange
        String imageUrl = "https://example.com/custom-image.jpg";
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName("Custom Category");
        inputDto.setImageUrl(imageUrl);

        ProductCategory entityToSave = new ProductCategory();
        entityToSave.setName("Custom Category");
        entityToSave.setImageUrl(imageUrl);

        ProductCategory savedEntity = new ProductCategory();
        savedEntity.setId(3L);
        savedEntity.setName("Custom Category");
        savedEntity.setImageUrl(imageUrl);

        ProductCategoryDto resultDto = new ProductCategoryDto();
        resultDto.setId(3L);
        resultDto.setName("Custom Category");
        resultDto.setImageUrl(imageUrl);

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(entityToSave);
        when(productCategoryRepository.save(entityToSave)).thenReturn(savedEntity);
        when(productCategoryMapper.toDto(savedEntity)).thenReturn(resultDto);

        // Act
        ProductCategoryDto result = productCategoryService.create(inputDto);

        // Assert
        assertThat(result.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    void shouldMapDtoToEntityBeforeSaving() {
        // Arrange
        ProductCategoryDto inputDto = new ProductCategoryDto();
        inputDto.setName("Test Category");

        when(productCategoryMapper.toEntity(inputDto)).thenReturn(testEntity);
        when(productCategoryRepository.save(testEntity)).thenReturn(testEntity);
        when(productCategoryMapper.toDto(testEntity)).thenReturn(testDto);

        // Act
        productCategoryService.create(inputDto);

        // Assert
        verify(productCategoryMapper).toEntity(inputDto);
        verify(productCategoryRepository).save(testEntity);
    }

    @Test
    void shouldReturnMultipleCategoriesInCorrectOrder() {
        // Arrange
        ProductCategory category1 = new ProductCategory();
        category1.setId(1L);
        category1.setName("First");

        ProductCategory category2 = new ProductCategory();
        category2.setId(2L);
        category2.setName("Second");

        ProductCategory category3 = new ProductCategory();
        category3.setId(3L);
        category3.setName("Third");

        ProductCategoryDto dto1 = new ProductCategoryDto();
        dto1.setId(1L);
        dto1.setName("First");

        ProductCategoryDto dto2 = new ProductCategoryDto();
        dto2.setId(2L);
        dto2.setName("Second");

        ProductCategoryDto dto3 = new ProductCategoryDto();
        dto3.setId(3L);
        dto3.setName("Third");

        when(productCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2, category3));
        when(productCategoryMapper.toDto(category1)).thenReturn(dto1);
        when(productCategoryMapper.toDto(category2)).thenReturn(dto2);
        when(productCategoryMapper.toDto(category3)).thenReturn(dto3);

        // Act
        List<ProductCategoryDto> result = productCategoryService.getAll();

        // Assert
        assertThat(result)
                .hasSize(3)
                .extracting(ProductCategoryDto::getId)
                .containsExactly(1L, 2L, 3L);
    }
}

