package com.ta.orders.model.documents.order;

import com.ta.orders.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "order_product_rows")
@Entity
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderProductsRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "count")
    private BigDecimal count;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;
}
