package kr.magicbox.media.application.dto.command;

import kr.magicbox.media.domain.vo.UserId;
import lombok.Builder;

@Builder
public record GenerateUploadUrlCommand(
        UserId uploaderId,
        String fileName,
        String contentType
) {}
