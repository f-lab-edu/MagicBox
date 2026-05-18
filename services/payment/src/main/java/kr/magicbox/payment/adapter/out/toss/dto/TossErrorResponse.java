package kr.magicbox.payment.adapter.out.toss.dto;

public record TossErrorResponse(
        String code,
        String message
) {
}
