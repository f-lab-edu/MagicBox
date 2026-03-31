package kr.magicbox.auth.adapter.out.persistence.repository;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthDomainEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthDomainEventRepository extends JpaRepository<AuthDomainEventEntity, String> {
}
