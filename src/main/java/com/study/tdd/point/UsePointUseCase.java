package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.study.tdd.point.TransactionType.USE;

@Service
@RequiredArgsConstructor
public class UsePointUseCase {

    private final LockManager lockManager;
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint use(Long userId, Long amount, Long updateMillis) {
        UserPoint usedUserPoint;
        lockManager.lock(userId);
        try {
            UserPoint userPoint = userPointTable.selectById(userId);
            usedUserPoint = userPoint.use(amount, updateMillis);
            userPointTable.insertOrUpdate(userId, usedUserPoint.point());
        } finally {
            lockManager.unlock(userId);
        }
        pointHistoryTable.insert(userId, amount, USE, updateMillis);
        return usedUserPoint;
    }
}
