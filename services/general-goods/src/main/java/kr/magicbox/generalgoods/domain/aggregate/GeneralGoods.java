package kr.magicbox.generalgoods.domain.aggregate;

import kr.magicbox.generalgoods.domain.enums.GeneralGoodsLevel;
import kr.magicbox.generalgoods.domain.enums.MagicGenre;
import kr.magicbox.generalgoods.domain.exception.InvalidFieldException;
import kr.magicbox.generalgoods.domain.vo.CreatorId;
import kr.magicbox.generalgoods.domain.vo.GeneralGoodsId;
import kr.magicbox.generalgoods.domain.vo.GeneralGoodsMedia;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class GeneralGoods {
    private final GeneralGoodsId id;
    private final CreatorId creatorId;
    private String name;
    private Long price;
    private Long stock;
    private String description;
    private GeneralGoodsLevel level;
    private Set<MagicGenre> categories;
    private List<GeneralGoodsMedia> generalGoodsMediaList;
    private boolean isDeleted;

    @Builder(builderMethodName = "createBuilder")
    public GeneralGoods(CreatorId creatorId, String name, Long price, Long stock, String description, GeneralGoodsLevel level,
                        Set<MagicGenre> categories, List<GeneralGoodsMedia> generalGoodsMediaList) {
        validateCreate(creatorId, name, price, stock, categories, generalGoodsMediaList);
        this.id = null;
        this.creatorId = creatorId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.level = level;
        this.categories = categories;
        this.generalGoodsMediaList = generalGoodsMediaList;
        this.isDeleted = false;
    }

    @Builder(builderMethodName = "reconstructBuilder")
    public GeneralGoods(GeneralGoodsId id, CreatorId creatorId, String name, Long price, Long stock,
                        String description, GeneralGoodsLevel level, Set<MagicGenre> categories,
                        List<GeneralGoodsMedia> generalGoodsMediaList, boolean isDeleted) {
        validateReconstruct(id, creatorId);
        this.id = id;
        this.creatorId = creatorId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.level = level;
        this.categories = categories;
        this.generalGoodsMediaList = generalGoodsMediaList;
        this.isDeleted = isDeleted;
    }

    public void delete() {
        this.isDeleted = true;
    }

    private void validateCreate(CreatorId creatorId, String name, Long price, Long stock,
                               Set<MagicGenre> categories, List<GeneralGoodsMedia> generalGoodsMediaList) {
        if (creatorId == null) throw new InvalidFieldException("크리에이터 ID는 필수입니다.");
        if (name == null || name.isEmpty()) throw new InvalidFieldException("이름은 필수입니다.");
        if (price == null || price <= 0) throw new InvalidFieldException("가격은 양수여야 합니다.");
        if (stock == null || stock < 0) throw new InvalidFieldException("재고는 0 이상이어야 합니다.");
        if (categories == null || categories.isEmpty()) throw new InvalidFieldException("카테고리는 하나 이상 필수입니다.");
        if (generalGoodsMediaList == null || generalGoodsMediaList.isEmpty()) throw new InvalidFieldException("미디어는 하나 이상 필수입니다.");
    }

    private void validateReconstruct(GeneralGoodsId id, CreatorId creatorId) {
        if (id == null) throw new InvalidFieldException("상품 ID는 필수입니다.");
        if (creatorId == null) throw new InvalidFieldException("크리에이터 ID는 필수입니다.");
    }

    public void update(String name, Long price, Long stock, String description, GeneralGoodsLevel level, Set<MagicGenre> categories, List<GeneralGoodsMedia> mediaList) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (stock != null) this.stock = stock;
        if (description != null) this.description = description;
        if (level != null) this.level = level;
        if (categories != null) this.categories = categories;
        if (mediaList != null) this.generalGoodsMediaList = mediaList;
    }
}
