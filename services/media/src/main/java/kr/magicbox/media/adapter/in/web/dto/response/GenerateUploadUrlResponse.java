package kr.magicbox.media.adapter.in.web.dto.response;

import kr.magicbox.media.application.dto.result.GenerateUploadUrlResult;
import lombok.Builder;

@Builder
public record GenerateUploadUrlResponse(
        Long mediaId,
        String uuid,
        String uploadUrl,
        long expiresIn
) {
    public static GenerateUploadUrlResponse from(GenerateUploadUrlResult result) {
        return GenerateUploadUrlResponse.builder()
                .mediaId(result.mediaId())
                .uuid(result.uuid())
                .uploadUrl(result.uploadUrl())
                .expiresIn(result.expiresIn())
                .build();
    }
}
