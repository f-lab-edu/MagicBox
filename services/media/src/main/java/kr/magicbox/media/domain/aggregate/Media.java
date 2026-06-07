package kr.magicbox.media.domain.aggregate;

import kr.magicbox.media.domain.enums.MediaStatus;
import kr.magicbox.media.domain.vo.MediaId;
import kr.magicbox.media.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Media {

    private final MediaId id;
    private final UserId uploaderId;
    private final String uuid;
    private final String fileName;
    private final String contentType;
    private MediaStatus status;

    @Builder
    public Media(MediaId id, UserId uploaderId, String uuid, String fileName,
                 String contentType, MediaStatus status) {
        this.id = id;
        this.uploaderId = uploaderId;
        this.uuid = uuid;
        this.fileName = fileName;
        this.contentType = contentType;
        this.status = status;
    }

    public void activate() {
        this.status = MediaStatus.ACTIVE;
    }
}
