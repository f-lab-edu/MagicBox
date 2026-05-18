package kr.magicbox.waiting.application.port.out;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WaitingQueuePort {

    /** NX 방식으로 대기열에 추가. 이미 있으면 false 반환 */
    Mono<Boolean> enqueue(ReleaseId releaseId, UserId userId, long score);

    /** 대기열에서 현재 순위 조회 (0-based). 없으면 empty */
    Mono<Long> getRank(ReleaseId releaseId, UserId userId);

    /** 대기열 전체 인원 수 */
    Mono<Long> getQueueSize(ReleaseId releaseId);

    /** 대기열에서 제거 */
    Mono<Void> dequeue(ReleaseId releaseId, UserId userId);

    /** 대기열 앞에서 N명 조회 (0-based, score 오름차순) */
    Flux<UserId> peekFront(ReleaseId releaseId, int count);

    /** 대기열 앞에서 N명 제거 */
    Mono<Long> removeFront(ReleaseId releaseId, int count);

    /** 대기열 전체 삭제 */
    Mono<Void> clear(ReleaseId releaseId);

    /** userId로 현재 대기 중인 releaseId 조회 */
    Mono<ReleaseId> findReleaseIdByUserId(UserId userId);
}
