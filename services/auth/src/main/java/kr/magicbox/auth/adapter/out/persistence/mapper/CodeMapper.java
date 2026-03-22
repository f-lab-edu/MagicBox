package kr.magicbox.auth.adapter.out.persistence.mapper;

import kr.magicbox.auth.adapter.out.persistence.entity.CodeEntity;
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
                .isNewUser(code.isNewUser())
                .expiresAt(code.getExpiresAt())
                .createdAt(code.getCreatedAt())
                .build();
    }

    public Code toDomain(CodeEntity entity) {
        return Code.builder()
                .code(entity.getCode())
                .userId(UserId.of(entity.getUserId()))
                .role(UserRole.of(entity.getRole()))
                .isNewUser(Boolean.TRUE.equals(entity.getIsNewUser()))
                .expiresAt(entity.getExpiresAt())
                .build();
    }
}