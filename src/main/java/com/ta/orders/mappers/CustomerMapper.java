package com.ta.orders.mappers;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "expeditorId", target = "expeditor.id")
    @Mapping(target = "creationTime", ignore = true)
    Customer toEntity(CustomerDto dto);

    @Mapping(source = "expeditor.id", target = "expeditorId")
    CustomerDto toDto(Customer entity);
}

