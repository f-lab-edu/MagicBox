package kr.magicbox.auth.adapter.out.cache.repository;

import kr.magicbox.auth.adapter.out.cache.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenEntity, Long> {
}
