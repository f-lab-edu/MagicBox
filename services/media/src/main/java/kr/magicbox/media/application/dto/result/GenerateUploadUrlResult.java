package kr.magicbox.media.application.dto.result;

import lombok.Builder;

@Builder
public record GenerateUploadUrlResult(
        Long mediaId,
        String uuid,
        String uploadUrl,
        long expiresIn
) {}
