package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.study.tdd.point.TransactionType.USE;

@Service
@RequiredArgsConstructor
public class UsePointUseCase {

    private final Lock lock = new ReentrantLock(true);

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint use(Long userId, Long amount, Long updateMillis) {
        UserPoint usedUserPoint;
        lock.lock();
        try {
            UserPoint userPoint = userPointTable.selectById(userId);
            usedUserPoint = userPoint.use(amount, updateMillis);
            userPointTable.insertOrUpdate(userId, usedUserPoint.point());
        } finally {
            lock.unlock();
        }
        pointHistoryTable.insert(userId, amount, USE, updateMillis);
        return usedUserPoint;
    }
}
