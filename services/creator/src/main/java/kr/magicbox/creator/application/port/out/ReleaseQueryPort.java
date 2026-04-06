package kr.magicbox.creator.application.port.out;

import java.util.List;

public interface ReleaseQueryPort {

    long getReleaseCount(Long creatorId);

    List<Object> getReleases(Long creatorId);
}
