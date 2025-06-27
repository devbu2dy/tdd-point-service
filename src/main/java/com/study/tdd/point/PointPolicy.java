package com.study.tdd.point;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointPolicy {
    MIN_CHARGE_AMOUNT(10_000),
    CHARGE_UNIT(1_000),
    MAX_CHARGE_PER_OPERATION(1_000_000),
    MAX_BALANCE(5_000_000),
    MIN_USE_AMOUNT(5_000);

    private final long value;

    public long value() {
        return value;
    }

    public String formattedValue() {
        return String.format("%,d", value);
    }
}
