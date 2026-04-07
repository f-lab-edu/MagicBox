package kr.magicbox.creator.application.service;

import kr.magicbox.creator.application.dto.command.UnbanCreatorCommand;
import kr.magicbox.creator.application.port.in.UnbanCreatorUseCase;
import kr.magicbox.creator.application.port.out.CreatorDomainEventRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.event.CreatorUnbannedEvent;
import kr.magicbox.creator.domain.exception.CreatorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UnbanCreatorService implements UnbanCreatorUseCase {

    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort creatorDomainEventRepositoryPort;

    @Override
    @Transactional
    public void unbanCreator(UnbanCreatorCommand command) {
        Creator creator = creatorRepositoryPort.findByNicknameWithLock(command.nickname())
                .orElseThrow(CreatorNotFoundException::new);

        creator.unban();
        creatorRepositoryPort.update(creator);

        CreatorUnbannedEvent event = CreatorUnbannedEvent.builder()
                .creatorId(creator.getId())
                .unbannedAt(Instant.now())
                .build();
        creatorDomainEventRepositoryPort.save(event);
    }
}
