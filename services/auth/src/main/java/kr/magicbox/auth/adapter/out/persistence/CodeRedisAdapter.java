package kr.magicbox.auth.adapter.out.persistence;

import kr.magicbox.auth.adapter.out.persistence.mapper.CodeMapper;
import kr.magicbox.auth.adapter.out.persistence.repository.CodeRedisRepository;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.domain.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CodeRedisAdapter implements CodeRepository {
    private final CodeRedisRepository codeRedisRepository;
    private final CodeMapper codeMapper;

    @Override
    public Optional<Code> getCodeByValue(String code) {
        return codeRedisRepository.findById(code)
                .map(codeMapper::toDomain);
    }

    @Override
    public void deleteCode(String code) {
        codeRedisRepository.deleteById(code);
    }
}