package kr.magicbox.creator.adapter.in.web;

import kr.magicbox.creator.adapter.in.web.constants.CursorConstants;
import kr.magicbox.creator.adapter.in.web.dto.response.CursorResponse;
import kr.magicbox.creator.adapter.in.web.dto.response.PendingCertificationResponse;
import kr.magicbox.creator.adapter.in.web.validation.CursorSize;
import kr.magicbox.creator.application.dto.query.GetAllPendingCertificationsQuery;
import kr.magicbox.creator.application.port.in.GetAllPendingCreatorCertificationsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/creator/certification")
@RequiredArgsConstructor
@Validated
public class AdminCreatorCertificationQueryController {

    private final GetAllPendingCreatorCertificationsUseCase getAllPendingCreatorCertificationsUseCase;

    @GetMapping("/pending")
    public ResponseEntity<CursorResponse<PendingCertificationResponse>> getPendingCertifications(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) @CursorSize Integer size
    ) {
        List<PendingCertificationResponse> content =
                getAllPendingCreatorCertificationsUseCase
                        .getAllPendingCreatorCertifications(GetAllPendingCertificationsQuery.of(cursor, size))
                        .stream()
                        .map(PendingCertificationResponse::from)
                        .toList();

        return ResponseEntity.ok(CursorResponse.of(content, size, PendingCertificationResponse::certificationId));
    }
}
