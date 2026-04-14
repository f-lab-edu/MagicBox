package kr.magicbox.generalgoods.adapter.out.persistence;

import kr.magicbox.generalgoods.adapter.out.persistence.entity.GeneralGoodsEntity;
import kr.magicbox.generalgoods.adapter.out.persistence.entity.GeneralGoodsMediaEntity;
import kr.magicbox.generalgoods.adapter.out.persistence.mapper.GeneralGoodsMapper;
import kr.magicbox.generalgoods.adapter.out.persistence.repository.GeneralGoodsJpaRepository;
import kr.magicbox.generalgoods.application.port.out.GeneralGoodsRepositoryPort;
import kr.magicbox.generalgoods.domain.aggregate.GeneralGoods;
import kr.magicbox.generalgoods.domain.exception.GeneralGoodsNotFoundException;
import kr.magicbox.generalgoods.domain.vo.CreatorId;
import kr.magicbox.generalgoods.domain.vo.GeneralGoodsId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralGoodsJpaAdapter implements GeneralGoodsRepositoryPort {
    private final GeneralGoodsJpaRepository generalGoodsJpaRepository;
    private final GeneralGoodsMapper generalGoodsMapper;

    @Override
    public void save(GeneralGoods generalGoods) {
        generalGoodsJpaRepository.save(generalGoodsMapper.toEntity(generalGoods));
    }

    @Override
    public void update(GeneralGoods generalGoods) {
        GeneralGoodsEntity entity = generalGoodsJpaRepository.findByIdAndIsDeletedFalse(generalGoods.getId().value())
                .orElseThrow(GeneralGoodsNotFoundException::new);
        entity.updateFromDomain(generalGoods);

        entity.getGeneralGoodsMediaList().clear();
        generalGoods.getGeneralGoodsMediaList().forEach(media -> {
            GeneralGoodsMediaEntity mediaEntity = GeneralGoodsMediaEntity.builder()
                    .mediaUrl(media.getMediaUrl())
                    .sortOrder(media.getSortOrder())
                    .build();
            entity.addMedia(mediaEntity);
        });
    }

    @Override
    public void delete(GeneralGoodsId id) {
        int affected = generalGoodsJpaRepository.softDeleteById(id.value());
        if (affected == 0) {
            throw new GeneralGoodsNotFoundException();
        }
    }

    @Override
    public void softDeleteByCreatorId(CreatorId creatorId) {
        generalGoodsJpaRepository.softDeleteByCreatorId(creatorId.value());
    }

    @Override
    public GeneralGoods findById(GeneralGoodsId id) {
        return generalGoodsJpaRepository.findByIdAndIsDeletedFalse(id.value())
                .map(generalGoodsMapper::toDomain)
                .orElseThrow(GeneralGoodsNotFoundException::new);
    }
}
