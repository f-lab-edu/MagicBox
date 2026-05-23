package kr.magicbox.delivery.adapter.out.persistence.repository;

import kr.magicbox.delivery.adapter.out.persistence.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, Long> {

    @Query("SELECT CASE WHEN EXISTS (SELECT d FROM DeliveryEntity d WHERE d.orderLineId = :orderLineId AND d.isDeleted = false) THEN true ELSE false END")
    boolean existsByOrderLineIdAndIsDeletedFalse(@Param("orderLineId") Long orderLineId);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.orderLineId = :orderLineId AND d.isDeleted = false")
    Optional<DeliveryEntity> findByOrderLineIdAndIsDeletedFalse(@Param("orderLineId") Long orderLineId);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.id = :id AND d.isDeleted = false")
    Optional<DeliveryEntity> findByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.status IN (kr.magicbox.delivery.domain.enums.DeliveryStatus.SHIPPED, kr.magicbox.delivery.domain.enums.DeliveryStatus.IN_TRANSIT) AND d.isDeleted = false")
    List<DeliveryEntity> findAllInProgress();
}
