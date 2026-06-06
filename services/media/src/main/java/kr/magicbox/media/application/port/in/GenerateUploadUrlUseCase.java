package kr.magicbox.media.application.port.in;

import kr.magicbox.media.application.dto.command.GenerateUploadUrlCommand;
import kr.magicbox.media.application.dto.result.GenerateUploadUrlResult;

public interface GenerateUploadUrlUseCase {
    GenerateUploadUrlResult generateUploadUrl(GenerateUploadUrlCommand command);
}
