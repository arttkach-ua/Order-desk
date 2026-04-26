package com.ta.orders.repository;

import com.ta.orders.model.Expeditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpeditorRepository extends JpaRepository<Expeditor, Long> {
}

