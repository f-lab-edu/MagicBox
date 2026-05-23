package kr.magicbox.payment.domain.aggregate;

import kr.magicbox.payment.domain.exception.InvalidFieldException;
import kr.magicbox.payment.domain.vo.PaymentLineId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentLine {

    private final PaymentLineId id;
    private final Long orderLineId;
    private final Long sellerId;
    private final Long amount;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public PaymentLine(Long orderLineId, Long sellerId, Long amount) {
        validateCreate(orderLineId, sellerId, amount);
        this.id = null;
        this.orderLineId = orderLineId;
        this.sellerId = sellerId;
        this.amount = amount;
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public PaymentLine(PaymentLineId id, Long orderLineId, Long sellerId, Long amount) {
        validateReconstruct(id, orderLineId, sellerId, amount);
        this.id = id;
        this.orderLineId = orderLineId;
        this.sellerId = sellerId;
        this.amount = amount;
    }

    private void validateCreate(Long orderLineId, Long sellerId, Long amount) {
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (sellerId == null || sellerId <= 0) throw new InvalidFieldException("판매자 ID는 양수여야 합니다.");
        if (amount == null || amount <= 0) throw new InvalidFieldException("결제 금액은 0보다 커야 합니다.");
    }

    private void validateReconstruct(PaymentLineId id, Long orderLineId, Long sellerId, Long amount) {
        if (id == null) throw new InvalidFieldException("결제 라인 ID는 필수입니다.");
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (sellerId == null || sellerId <= 0) throw new InvalidFieldException("판매자 ID는 양수여야 합니다.");
        if (amount == null || amount <= 0) throw new InvalidFieldException("결제 금액은 0보다 커야 합니다.");
    }
}
