package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.study.tdd.point.TransactionType.CHARGE;

@Service
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final LockManager lockManager;
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint charge(Long userId, Long amount, Long updateMillis) {
        UserPoint chargedUserPoint;
        lockManager.lock(userId);
        try {
            UserPoint userPoint = userPointTable.selectById(userId);
            chargedUserPoint = userPoint.charge(amount, updateMillis);
            userPointTable.insertOrUpdate(userId, chargedUserPoint.point());
        } finally {
            lockManager.unlock(userId);
            lockManager.cleanUnusedLocks();
        }
        pointHistoryTable.insert(userId, amount, CHARGE, updateMillis);
        return chargedUserPoint;
    }
}
