package com.ta.orders.repository;

import com.ta.orders.model.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceTypeRepository extends JpaRepository<PriceType, Long> {

}
