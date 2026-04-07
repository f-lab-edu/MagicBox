package kr.magicbox.creator.application.port.out;

import java.util.List;

public interface ShortformQueryPort {

    List<Object> getShortforms(Long creatorId);
}
