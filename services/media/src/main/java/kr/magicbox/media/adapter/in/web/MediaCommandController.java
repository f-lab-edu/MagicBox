package kr.magicbox.media.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.media.adapter.in.web.dto.request.GenerateUploadUrlRequest;
import kr.magicbox.media.adapter.in.web.dto.response.GenerateUploadUrlResponse;
import kr.magicbox.media.application.port.in.DeleteMediaUseCase;
import kr.magicbox.media.application.port.in.GenerateUploadUrlUseCase;
import kr.magicbox.media.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaCommandController {

    private final GenerateUploadUrlUseCase generateUploadUrlUseCase;
    private final DeleteMediaUseCase deleteMediaUseCase;

    @PostMapping("/upload-url")
    public ResponseEntity<GenerateUploadUrlResponse> generateUploadUrl(
            @AuthenticationPrincipal UserId uploaderId,
            @Valid @RequestBody GenerateUploadUrlRequest request
    ) {
        return ResponseEntity.ok(
                GenerateUploadUrlResponse.from(generateUploadUrlUseCase.generateUploadUrl(request.toCommand(uploaderId)))
        );
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteMedia(
            @AuthenticationPrincipal UserId uploaderId,
            @PathVariable String uuid
    ) {
        deleteMediaUseCase.delete(uuid, uploaderId);
        return ResponseEntity.noContent().build();
    }
}
