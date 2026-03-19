package kr.magicbox.auth.domain.repository;

import kr.magicbox.auth.domain.aggregate.Code;

import java.util.Optional;

public interface CodeRepository {
    Optional<Code> getCodeByValue(String code);
    void deleteCode(String code);
}