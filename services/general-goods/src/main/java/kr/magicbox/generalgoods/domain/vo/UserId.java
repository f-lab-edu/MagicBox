package kr.magicbox.generalgoods.domain.vo;

public record UserId(Long value) {

    public UserId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("사용자 ID는 양수여야 합니다.");
        }
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }
}