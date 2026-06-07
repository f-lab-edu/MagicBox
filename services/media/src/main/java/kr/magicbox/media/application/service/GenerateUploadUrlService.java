package kr.magicbox.media.application.service;

import kr.magicbox.media.application.dto.command.GenerateUploadUrlCommand;
import kr.magicbox.media.application.dto.result.GenerateUploadUrlResult;
import kr.magicbox.media.application.port.in.GenerateUploadUrlUseCase;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.application.port.out.ObjectStoragePort;
import kr.magicbox.media.domain.aggregate.Media;
import kr.magicbox.media.domain.enums.MediaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenerateUploadUrlService implements GenerateUploadUrlUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final ObjectStoragePort objectStoragePort;

    @Value("${minio.presigned-url-ttl-seconds}")
    private long presignedUrlTtlSeconds;

    @Transactional
    @Override
    public GenerateUploadUrlResult generateUploadUrl(GenerateUploadUrlCommand command) {
        String uuid = UUID.randomUUID().toString();

        Media media = Media.builder()
                .uploaderId(command.uploaderId())
                .uuid(uuid)
                .fileName(command.fileName())
                .contentType(command.contentType())
                .status(MediaStatus.INACTIVE)
                .build();

        Media saved = mediaRepositoryPort.save(media);

        String uploadUrl = objectStoragePort.generatePresignedPutUrl(uuid, command.contentType(), presignedUrlTtlSeconds);

        return GenerateUploadUrlResult.builder()
                .mediaId(saved.getId().value())
                .uuid(uuid)
                .uploadUrl(uploadUrl)
                .expiresIn(presignedUrlTtlSeconds)
                .build();
    }
}
