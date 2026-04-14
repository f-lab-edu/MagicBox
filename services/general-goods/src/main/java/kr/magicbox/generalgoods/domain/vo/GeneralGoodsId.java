package kr.magicbox.generalgoods.domain.vo;

public record GeneralGoodsId(Long value) {
    public static GeneralGoodsId of(Long value) {
        return new GeneralGoodsId(value);
    }
}