package com.ta.orders.mappers.documents;

import com.ta.orders.dto.order.rows.OrderProductsRowRequestDto;
import com.ta.orders.dto.order.rows.OrderProductsRowResponseDto;
import com.ta.orders.model.Product;
import com.ta.orders.model.documents.order.OrderProductsRow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class OrderProductsRowMapper {

    @PersistenceContext
    protected EntityManager entityManager;

    @Mapping(target = "product", expression = "java(getProduct(dto.productId()))")
    @Mapping(target = "order", ignore = true)
    public abstract OrderProductsRow toEntity(OrderProductsRowRequestDto dto);

    protected Product getProduct(long id) {
        return entityManager.getReference(Product.class, id);
    }

    @Mapping(target = "product", expression = "java(new ReferenceDto(entity.getProduct().getId(), entity.getProduct().getName()))")
    public abstract OrderProductsRowResponseDto toDto(OrderProductsRow entity);
}
