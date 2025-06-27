package com.study.tdd.point;

import static com.study.tdd.point.PointPolicy.*;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint charge(Long amount, Long updateMillis) {
        verifyChargeAmount(amount, point);
        return new UserPoint(id, point + amount, updateMillis);
    }

    private void verifyChargeAmount(Long amount, Long balance) {
        if (amount < MIN_CHARGE_AMOUNT.value()) {
            throw new IllegalArgumentException(
                    "포인트는 최소 " + MIN_CHARGE_AMOUNT.formattedValue() + " 포인트 이상부터 충전할 수 있습니다."
            );
        }

        if (amount % CHARGE_UNIT.value() != 0) {
            throw new IllegalArgumentException(
                    "포인트 충전은 " + CHARGE_UNIT.formattedValue() + " 포인트 단위로만 가능합니다."
            );
        }

        if (amount > MAX_CHARGE_PER_OPERATION.value()) {
            throw new IllegalArgumentException(
                    "포인트는 최대 " + MAX_CHARGE_PER_OPERATION.formattedValue() + " 포인트까지 한 번에 충전할 수 있습니다."
            );
        }

        long totalAmount = balance + amount;
        if (totalAmount > MAX_BALANCE.value()) {
            throw new IllegalArgumentException(
                    "포인트 잔고가 최대 한도(" + MAX_BALANCE.formattedValue() + " 포인트)를 초과하여 충전이 불가합니다."
            );
        }
    }

    public UserPoint use(Long amount, Long updateMillis) {
        verifyUseAmount(amount, point);
        return new UserPoint(id, point - amount, updateMillis);
    }

    private void verifyUseAmount(Long amount, Long balance) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 포인트는 0보다 커야 합니다.");
        }

        if (amount < MIN_USE_AMOUNT.value()) {
            throw new IllegalArgumentException(
                    "포인트는 최소 " + MIN_USE_AMOUNT.formattedValue() + " 포인트 이상부터 사용할 수 있습니다."
            );
        }

        if (amount > balance) {
            throw new IllegalArgumentException(
                    String.format("잔고가 부족합니다. (현재 잔고: %,d 포인트, 사용 요청: %,d 포인트)", balance, amount)
            );
        }
    }
}
