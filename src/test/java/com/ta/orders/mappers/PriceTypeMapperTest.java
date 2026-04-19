package com.ta.orders.mappers;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.model.PriceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTypeMapperTest {

    private static final String RETAIL = "Retail";
    private PriceTypeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PriceTypeMapper.class);
    }

    @Test
    void shouldMapDtoToEntity() {
        PriceTypeDto dto = new PriceTypeDto(1L, RETAIL);

        PriceType entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo(RETAIL);
        assertThat(entity.isDefault()).isFalse();
    }

    @Test
    void shouldMapEntityToDto() {
        PriceType entity = new PriceType();
        entity.setId(2L);
        entity.setName(RETAIL);
        entity.setDefault(true);
        entity.setCreationTime(LocalDateTime.now());

        PriceTypeDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo(RETAIL);
    }
}