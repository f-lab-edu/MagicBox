package kr.magicbox.creator.application.service;

import kr.magicbox.creator.application.dto.command.HandleUserBannedCommand;
import kr.magicbox.creator.application.port.in.HandleUserBannedUseCase;
import kr.magicbox.creator.application.port.out.CreatorDomainEventRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.event.CreatorRevokedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HandleUserBannedService implements HandleUserBannedUseCase {

    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort eventRepositoryPort;

    @Override
    @Transactional
    public void handleUserBanned(HandleUserBannedCommand command) {
        Optional<Creator> creatorOpt = creatorRepositoryPort.findByUserIdWithLock(command.userId());
        if (creatorOpt.isEmpty()) return;
        Creator creator = creatorOpt.get();
        creator.ban();
        creatorRepositoryPort.update(creator);
        eventRepositoryPort.save(
                CreatorRevokedEvent.builder()
                        .creatorId(creator.getId())
                        .revokedAt(Instant.now())
                        .build()
        );
    }
}
