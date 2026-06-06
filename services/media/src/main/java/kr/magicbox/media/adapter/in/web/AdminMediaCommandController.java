package kr.magicbox.media.adapter.in.web;

import kr.magicbox.media.application.port.in.DeleteMediaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/media")
@RequiredArgsConstructor
public class AdminMediaCommandController {

    private final DeleteMediaUseCase deleteMediaUseCase;

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String uuid) {
        deleteMediaUseCase.deleteAsAdmin(uuid);
        return ResponseEntity.noContent().build();
    }
}
