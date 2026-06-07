package kr.magicbox.media.application.port.in;

import java.util.List;

public interface DeleteMediaByUuidsUseCase {
    void deleteByUuids(List<String> uuids);
}
