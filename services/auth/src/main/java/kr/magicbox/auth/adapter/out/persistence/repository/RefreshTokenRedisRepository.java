package kr.magicbox.auth.adapter.out.persistence.repository;

import kr.magicbox.auth.adapter.out.persistence.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenEntity, Long> {
}