package kr.magicbox.auth.adapter.out.cache.repository;

import kr.magicbox.auth.adapter.out.cache.entity.CodeEntity;
import org.springframework.data.repository.CrudRepository;

public interface CodeRedisRepository extends CrudRepository<CodeEntity, String> {
}
