package kr.magicbox.delivery.adapter.out.persistence.repository;

import kr.magicbox.delivery.adapter.out.persistence.entity.TrackingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackingHistoryJpaRepository extends JpaRepository<TrackingHistoryEntity, Long> {

    @Query("SELECT h FROM TrackingHistoryEntity h WHERE h.deliveryId = :deliveryId ORDER BY h.trackedAt ASC")
    List<TrackingHistoryEntity> findByDeliveryId(@Param("deliveryId") Long deliveryId);

    @Modifying
    @Query("DELETE FROM TrackingHistoryEntity h WHERE h.deliveryId = :deliveryId")
    void deleteByDeliveryId(@Param("deliveryId") Long deliveryId);
}
