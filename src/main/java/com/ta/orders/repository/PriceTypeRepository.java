package com.ta.orders.repository;

import com.ta.orders.model.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PriceTypeRepository extends JpaRepository<PriceType, Long> {

    @Query("SELECT pt FROM PriceType pt JOIN ClientPriceTypeConnection cptc ON pt.id = cptc.priceTypeId " +
           "WHERE cptc.clientId = :clientId AND pt.isDefault = false")
    Optional<PriceType> findByClientId(@Param("clientId") long clientId);

    Optional<PriceType> findByIsDefaultTrue();

    @Modifying
    @Transactional
    @Query("UPDATE PriceType pt SET pt.isDefault = false WHERE pt.isDefault = true AND pt.id != :priceTypeId")
    void clearDefaultForOthers(@Param("priceTypeId") long priceTypeId);

    @Modifying
    @Transactional
    @Query("UPDATE PriceType pt SET pt.isDefault = true WHERE pt.id = :priceTypeId")
    void setAsDefault(@Param("priceTypeId") long priceTypeId);
}
