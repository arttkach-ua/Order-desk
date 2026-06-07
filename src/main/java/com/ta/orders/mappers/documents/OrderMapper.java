package com.ta.orders.mappers.documents;

import com.ta.orders.dto.order.OrderRequestDto;
import com.ta.orders.dto.order.OrderResponseDto;
import com.ta.orders.dto.order.raws.OrderProductsRowRequestDto;
import com.ta.orders.dto.ReferenceDto;
import com.ta.orders.dto.order.raws.OrderProductsRowResponseDto;
import com.ta.orders.model.Customer;
import com.ta.orders.model.PriceType;
import com.ta.orders.model.documents.order.Order;
import com.ta.orders.model.documents.order.OrderProductsRow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderProductsRowMapper rowMapper;
    private final EntityManager entityManager;


    public Order toEntity(OrderRequestDto dto) {
        Order order = Order.builder()
                .date(dto.date())
                .comments(dto.comments())
                .client(getClient(dto.clientId()))
                .priceType(getPriceType(dto.priceTypeId()))
                .sum(dto.sum())
                .build();
        order.setProducts(mapProducts(dto.products(), order));
        return order;
    }

    public OrderResponseDto toDto(Order entity) {
        return new OrderResponseDto(
                entity.getId(),
                entity.getDate(),
                clientReference(entity.getClient()),
                priceTypeReference(entity.getPriceType()),
                entity.getSum(),
                entity.getComments(),
                getResponseRows(entity.getProducts())
        );
    }

    private List<OrderProductsRow> mapProducts(List<OrderProductsRowRequestDto> dtos, Order order) {
        return dtos.stream()
                .map(rowMapper::toEntity)
                .peek(row -> row.setOrder(order))
                .toList();
    }

    private Customer getClient(long id) {
        return entityManager.getReference(Customer.class, id);
    }

    private PriceType getPriceType(long id) {
        return entityManager.getReference(PriceType.class, id);
    }

    private ReferenceDto clientReference(Customer customer) {
        return new ReferenceDto(customer.getId(), customer.getName());
    }

    private ReferenceDto priceTypeReference(PriceType priceType) {
        return new ReferenceDto(priceType.getId(), priceType.getName());
    }

    private List<OrderProductsRowResponseDto> getResponseRows(List<OrderProductsRow> rows) {
        return rows
                .stream()
                .map(rowMapper::toDto)
                .toList();
    }
}