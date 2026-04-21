package com.ta.orders.repository;

import com.ta.orders.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findByProductIdAndPriceTypeAndIsCurrent(Long productId, String priceType, boolean isCurrent);

    @Query("SELECT DISTINCT p.productId FROM Price p WHERE p.productId IN :productIds")
    List<Long> findExistingProductIds(@Param("productIds") List<Long> productIds);

    List<Price> findByIsCurrent(boolean isCurrent);

    @Query("SELECT p FROM Price p WHERE p.isCurrent = true AND p.priceType = :priceType")
    List<Price> findAllCurrentByPriceType(@Param("priceType") String priceType);

    @Query("SELECT p FROM Price p WHERE p.productId = :productId AND p.priceType = :priceType " +
           "AND p.validFrom <= :timestamp AND (p.validTo IS NULL OR p.validTo > :timestamp)")
    Optional<Price> findValidPriceAt(@Param("productId") Long productId,
                                      @Param("priceType") String priceType,
                                      @Param("timestamp") LocalDateTime timestamp);

    @Modifying
    @Query("UPDATE Price p SET p.validTo = :validTo, p.isCurrent = false " +
           "WHERE p.productId = :productId AND p.priceType = :priceType AND p.isCurrent = true")
    void closeCurrentPrice(@Param("productId") Long productId,
                           @Param("priceType") String priceType,
                           @Param("validTo") LocalDateTime validTo);

    List<Price> findByProductIdAndPriceTypeOrderByValidFromDesc(Long productId, String priceType);
}

