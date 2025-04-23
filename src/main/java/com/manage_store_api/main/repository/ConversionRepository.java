package com.manage_store_api.main.repository;

import com.manage_store_api.main.model.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConversionRepository extends JpaRepository<Conversion, Long> {
    @Query("SELECT c FROM Conversion c WHERE c.store.id = :storeId ORDER BY c.createdAt DESC")
    List<Conversion> findByStoreIdOrderByCreatedAtAsc(@Param("storeId") Long storeId);

    @Transactional
    @Modifying
    @Query("UPDATE Conversion c SET c.isCurrentDate = :isCurrent, c.untilDate = CURRENT_TIMESTAMP WHERE c.store.id = :storeId")
    int deactivateCurrentConversions(@Param("storeId") Long storeId, @Param("isCurrent") boolean isCurrent);
}