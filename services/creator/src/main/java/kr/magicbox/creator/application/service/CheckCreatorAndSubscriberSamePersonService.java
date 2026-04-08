package kr.magicbox.creator.application.service;

import kr.magicbox.creator.application.dto.query.CheckCreatorAndSubscriberSamePersonQuery;
import kr.magicbox.creator.application.port.in.CheckCreatorAndSubscriberSamePersonUseCase;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckCreatorAndSubscriberSamePersonService implements CheckCreatorAndSubscriberSamePersonUseCase {
    private final CreatorRepositoryPort creatorRepositoryPort;

    @Transactional(readOnly = true)
    @Override
    public boolean isSamePerson(CheckCreatorAndSubscriberSamePersonQuery query) {
        return creatorRepositoryPort.findByCreatorId(query.creatorId())
                .map(creator -> creator.getUserIdValue().equals(query.subscriberId().value()))
                .orElse(false);
    }
}
