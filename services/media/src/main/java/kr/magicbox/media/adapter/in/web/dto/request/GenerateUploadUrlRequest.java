package kr.magicbox.media.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.media.application.dto.command.GenerateUploadUrlCommand;
import kr.magicbox.media.domain.vo.UserId;

public record GenerateUploadUrlRequest(
        @NotBlank(message = "파일명은 필수입니다.") String fileName,
        @NotBlank(message = "Content-Type은 필수입니다.") String contentType
) {
    public GenerateUploadUrlCommand toCommand(UserId uploaderId) {
        return GenerateUploadUrlCommand.builder()
                .uploaderId(uploaderId)
                .fileName(fileName)
                .contentType(contentType)
                .build();
    }
}
