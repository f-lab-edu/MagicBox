package kr.magicbox.auth.adapter.out.cache.mapper;

import kr.magicbox.auth.adapter.out.cache.entity.CodeEntity;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class CodeMapper {

    public CodeEntity toEntity(Code code) {
        return CodeEntity.builder()
                .code(code.getCode())
                .userId(code.getUserId().value())
                .role(code.getRole().name())
                .expiresAt(code.getExpiresAt())
                .createdAt(code.getCreatedAt())
                .build();
    }

    public Code toDomain(CodeEntity entity) {
        return Code.reconstructBuilder()
                .code(entity.getCode())
                .userId(UserId.of(entity.getUserId()))
                .role(UserRole.of(entity.getRole()))
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
