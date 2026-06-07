package com.ta.orders.repository;

import com.ta.orders.model.documents.order.Order;
import com.ta.orders.repository.documents.DocumentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, DocumentRepository {
    @Query("SELECT MAX(o.number) FROM Order o")
    Integer findMaxNumber();
}

