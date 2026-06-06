package kr.magicbox.media.application.port.in;

import java.util.List;

public interface ActivateMediaUseCase {
    void activateByUuids(List<String> uuids);
}
