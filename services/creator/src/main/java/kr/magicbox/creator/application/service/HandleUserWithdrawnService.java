package kr.magicbox.creator.application.service;

import kr.magicbox.creator.application.dto.command.HandleUserWithdrawnCommand;
import kr.magicbox.creator.application.port.in.HandleUserWithdrawnUseCase;
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
public class HandleUserWithdrawnService implements HandleUserWithdrawnUseCase {

    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort eventRepositoryPort;

    @Override
    @Transactional
    public void handleUserWithdrawn(HandleUserWithdrawnCommand command) {
        Optional<Creator> creatorOpt = creatorRepositoryPort.findByUserId(command.userId());
        if (creatorOpt.isEmpty()) return;
        Creator creator = creatorOpt.get();
        creator.delete();
        creatorRepositoryPort.update(creator);
        eventRepositoryPort.save(
                CreatorRevokedEvent.builder()
                        .creatorId(creator.getId())
                        .revokedAt(Instant.now())
                        .build()
        );
    }
}
