package kr.magicbox.subscribe.application.port.out;

import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface CreatorIdentityQueryPort {
    CompletableFuture<Boolean> isCreatorOwnedByUser(CreatorId creatorId, SubscriberId subscriberId) throws ExecutionException, InterruptedException;
}
