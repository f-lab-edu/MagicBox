package kr.magicbox.settlement.domain.vo;

import kr.magicbox.settlement.domain.exception.InvalidFieldException;

public record CreatorAccount(
        String bankCode,
        String accountNumber,
        String accountHolder
) {

    public CreatorAccount {
        if (bankCode == null || bankCode.isBlank()) {
            throw new InvalidFieldException("은행 코드는 필수 값입니다.");
        }
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new InvalidFieldException("계좌번호는 필수 값입니다.");
        }
        if (accountHolder == null || accountHolder.isBlank()) {
            throw new InvalidFieldException("예금주는 필수 값입니다.");
        }
    }

    public static CreatorAccount of(String bankCode, String accountNumber, String accountHolder) {
        return new CreatorAccount(bankCode, accountNumber, accountHolder);
    }
}
