package com.ta.orders.model.documents.order;

import com.ta.orders.model.Customer;
import com.ta.orders.model.PriceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
@Data
@ToString
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor
@EqualsAndHashCode
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Customer client;

    @ManyToOne
    @JoinColumn(name = "price_type_id", nullable = false)
    private PriceType priceType;

    @Column(name = "comments")
    private String comments;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime creationTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductsRow> products;
}

