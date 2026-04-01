package kr.magicbox.auth.adapter.out.cache;

import kr.magicbox.auth.adapter.out.cache.mapper.CodeMapper;
import kr.magicbox.auth.adapter.out.cache.repository.CodeRedisRepository;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.application.port.out.CodeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CodeRedisAdapter implements CodeRepositoryPort {
    private final CodeRedisRepository codeRedisRepository;
    private final CodeMapper codeMapper;

    @Override
    public Optional<Code> getCodeByValue(String code) {
        return codeRedisRepository.findById(code)
                .map(codeMapper::toDomain);
    }

    @Override
    public void save(Code code) {
        codeRedisRepository.save(codeMapper.toEntity(code));
    }

    @Override
    public void deleteById(String code) {
        codeRedisRepository.deleteById(code);
    }
}
