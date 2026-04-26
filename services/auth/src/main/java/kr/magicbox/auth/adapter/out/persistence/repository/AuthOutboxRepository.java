package kr.magicbox.auth.adapter.out.persistence.repository;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthOutboxRepository extends JpaRepository<AuthOutboxEntity, Long> {
}
