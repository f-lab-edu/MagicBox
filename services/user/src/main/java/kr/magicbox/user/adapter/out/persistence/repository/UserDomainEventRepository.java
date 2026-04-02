package kr.magicbox.user.adapter.out.persistence.repository;

import kr.magicbox.user.adapter.out.persistence.entity.UserDomainEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDomainEventRepository extends JpaRepository<UserDomainEventEntity, Long> {
}