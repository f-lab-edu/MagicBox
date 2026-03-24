package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.aggregate.Code;

import java.util.Optional;

public interface CodeRepositoryPort {
    Optional<Code> getCodeByValue(String code);
    void save(Code code);
    void deleteById(String code);
}