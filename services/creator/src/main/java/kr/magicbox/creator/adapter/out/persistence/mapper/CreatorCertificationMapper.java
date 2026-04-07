package kr.magicbox.creator.adapter.out.persistence.mapper;

import kr.magicbox.creator.adapter.out.persistence.entity.CreatorCertificationEntity;
import kr.magicbox.creator.adapter.out.persistence.vo.CreatorCertificationRequestVO;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import kr.magicbox.creator.domain.vo.CreatorCertificationRequest;
import kr.magicbox.creator.domain.vo.CreatorCertificationResult;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreatorCertificationMapper {

    private final CreatorCertificationRequestMapper certificationRequestMapper;
    private final CertificationResultMapper certificationResultMapper;

    public CreatorCertification toDomain(CreatorCertificationEntity entity) {
        CreatorCertificationRequest request = certificationRequestMapper.toDomain(entity.getRequest());
        CreatorCertificationResult result = Optional.ofNullable(entity.getResult())
                .map(certificationResultMapper::toDomain)
                .orElse(null);

        return CreatorCertification.builder()
                .id(CreatorCertificationId.of(entity.getId()))
                .userId(UserId.of(entity.getUserId()))
                .request(request)
                .status(entity.getStatus())
                .result(result)
                .build();
    }

    public CreatorCertificationEntity toEntity(CreatorCertification domain) {
        CreatorCertificationRequestVO requestVO = certificationRequestMapper.toEntity(domain.getRequest());
        return CreatorCertificationEntity.builder()
                .userId(domain.getUserId().value())
                .request(requestVO)
                .build();
    }

    public void updateEntity(CreatorCertification creatorCertification, CreatorCertificationEntity creatorCertificationEntity) {
        creatorCertificationEntity.updateFromDomain(creatorCertification);
    }
}