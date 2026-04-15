package kr.magicbox.creator.application.port.out;

import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.vo.Nickname;
import kr.magicbox.creator.domain.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface CreatorRepositoryPort {

    void save(Creator creator);
    void update(Creator creator);

    boolean existsByUserId(UserId userId);

    Optional<Creator> findByUserId(UserId userId);

    Optional<Creator> findByUserIdWithLock(UserId userId);

    Optional<Creator> findByNickname(Nickname nickname);

    Optional<Creator> findByNicknameWithLock(Nickname nickname);

    List<Creator> findAllByCursor(Long cursorId, int size);

    List<Creator> searchByNickname(String keyword, Long cursorId, int size);
}