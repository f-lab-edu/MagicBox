package kr.magicbox.payment.domain.aggregate;

import kr.magicbox.payment.domain.enums.PaymentStatus;
import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import kr.magicbox.payment.domain.exception.InvalidFieldException;
import kr.magicbox.payment.domain.vo.PaymentId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Payment {

    private final PaymentId id;
    private final Long orderId;
    private final Long customerId;
    private PaymentStatus status;
    private final Long totalAmount;
    private final String currency;
    private String paymentKey;
    private TossPaymentMethod paymentMethod;
    private final List<PaymentLine> paymentLines;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Payment(Long orderId, Long customerId, Long totalAmount,
                   String currency, List<PaymentLine> paymentLines) {
        validateCreate(orderId, customerId, totalAmount);
        this.id = null;
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = PaymentStatus.PENDING;
        this.totalAmount = totalAmount;
        this.currency = currency != null ? currency : "KRW";
        this.paymentKey = null;
        this.paymentMethod = null;
        this.paymentLines = paymentLines != null ? new ArrayList<>(paymentLines) : new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Payment(PaymentId id, Long orderId, Long customerId, PaymentStatus status,
                   Long totalAmount, String currency, String paymentKey,
                   TossPaymentMethod paymentMethod, List<PaymentLine> paymentLines,
                   Instant createdAt, Instant updatedAt) {
        validateReconstruct(id, orderId, customerId, status, totalAmount, currency, createdAt, updatedAt);
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.paymentLines = paymentLines != null ? new ArrayList<>(paymentLines) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validateCreate(Long orderId, Long customerId, Long totalAmount) {
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (customerId == null || customerId <= 0) throw new InvalidFieldException("구매자 ID는 양수여야 합니다.");
        if (totalAmount == null || totalAmount <= 0) throw new InvalidFieldException("결제 총액은 0보다 커야 합니다.");
    }

    private void validateReconstruct(PaymentId id, Long orderId, Long customerId, PaymentStatus status,
                                     Long totalAmount, String currency, Instant createdAt, Instant updatedAt) {
        if (id == null) throw new InvalidFieldException("결제 ID는 필수입니다.");
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (customerId == null || customerId <= 0) throw new InvalidFieldException("구매자 ID는 양수여야 합니다.");
        if (status == null) throw new InvalidFieldException("결제 상태는 필수입니다.");
        if (totalAmount == null || totalAmount <= 0) throw new InvalidFieldException("결제 총액은 0보다 커야 합니다.");
        if (currency == null || currency.isBlank()) throw new InvalidFieldException("통화는 필수입니다.");
        if (createdAt == null) throw new InvalidFieldException("생성 시각은 필수입니다.");
        if (updatedAt == null) throw new InvalidFieldException("수정 시각은 필수입니다.");
    }

    public void approve(String paymentKey, TossPaymentMethod paymentMethod) {
        validateStatus(PaymentStatus.PENDING);
        if (paymentKey == null || paymentKey.isBlank()) throw new InvalidFieldException("결제 키는 필수입니다.");
        if (paymentMethod == null) throw new InvalidFieldException("결제 수단은 필수입니다.");
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.APPROVED;
        this.updatedAt = Instant.now();
    }

    public void fail() {
        validateStatus(PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED;
        this.updatedAt = Instant.now();
    }

    public void requestCancel() {
        validateStatus(PaymentStatus.APPROVED);
        this.status = PaymentStatus.CANCEL_REQUESTED;
        this.updatedAt = Instant.now();
    }

    public void completeCancel() {
        validateStatus(PaymentStatus.CANCEL_REQUESTED);
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void failCancel() {
        validateStatus(PaymentStatus.CANCEL_REQUESTED);
        this.status = PaymentStatus.CANCELLATION_FAILED;
        this.updatedAt = Instant.now();
    }

    public List<PaymentLine> getPaymentLines() {
        return Collections.unmodifiableList(paymentLines);
    }

    private void validateStatus(PaymentStatus expected) {
        if (this.status != expected) {
            throw new InvalidFieldException("현재 상태에서 해당 작업을 수행할 수 없습니다. 현재: " + this.status + ", 기대: " + expected);
        }
    }
}
