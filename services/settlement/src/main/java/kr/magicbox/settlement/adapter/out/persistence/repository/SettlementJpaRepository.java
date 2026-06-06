package kr.magicbox.settlement.adapter.out.persistence.repository;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SettlementJpaRepository extends JpaRepository<SettlementEntity, Long> {

    @Query("SELECT s FROM SettlementEntity s WHERE s.orderLineId = :orderLineId")
    Optional<SettlementEntity> findByOrderLineId(@Param("orderLineId") Long orderLineId);

    @Query("SELECT s FROM SettlementEntity s WHERE s.orderId = :orderId")
    List<SettlementEntity> findByOrderId(@Param("orderId") Long orderId);
}
