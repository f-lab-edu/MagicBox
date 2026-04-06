package kr.magicbox.creator.application.service;

import kr.magicbox.creator.application.dto.command.HandleUserWithdrawnCommand;
import kr.magicbox.creator.application.port.in.HandleUserWithdrawnUseCase;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import kr.magicbox.creator.domain.aggregate.Creator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HandleUserWithdrawnService implements HandleUserWithdrawnUseCase {

    private final CreatorRepositoryPort creatorRepositoryPort;

    @Override
    @Transactional
    public void handleUserWithdrawn(HandleUserWithdrawnCommand command) {
        Optional<Creator> creatorOpt = creatorRepositoryPort.findByUserId(command.userId());
        if(creatorOpt.isEmpty()) return;
        Creator creator = creatorOpt.get();
        creator.delete();
        creatorRepositoryPort.save(creator);
    }
}
