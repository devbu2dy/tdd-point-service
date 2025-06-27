package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.study.tdd.point.TransactionType.CHARGE;

@Service
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint charge(Long userId, Long amount, Long updateMillis) {
        UserPoint userPoint = userPointTable.selectById(userId);
        UserPoint chargedUserPoint = userPoint.charge(amount, updateMillis);
        userPointTable.insertOrUpdate(userId, chargedUserPoint.point());
        pointHistoryTable.insert(userId, amount, CHARGE, updateMillis);
        return chargedUserPoint;
    }
}
