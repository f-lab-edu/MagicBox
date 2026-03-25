package kr.magicbox.auth.adapter.out.persistence.repository;

import kr.magicbox.auth.adapter.out.persistence.entity.CodeEntity;
import org.springframework.data.repository.CrudRepository;

public interface CodeRedisRepository extends CrudRepository<CodeEntity, String> {
}