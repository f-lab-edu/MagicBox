package kr.magicbox.creator.adapter.out.persistence.repository;

import kr.magicbox.creator.adapter.out.persistence.entity.CreatorDomainEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorDomainEventRepository extends JpaRepository<CreatorDomainEventEntity, Long> {
}