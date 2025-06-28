package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.study.tdd.point.TransactionType.CHARGE;

@Service
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final Lock lock = new ReentrantLock(true);

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint charge(Long userId, Long amount, Long updateMillis) {
        UserPoint chargedUserPoint;
        lock.lock();
        try {
            UserPoint userPoint = userPointTable.selectById(userId);
            chargedUserPoint = userPoint.charge(amount, updateMillis);
            userPointTable.insertOrUpdate(userId, chargedUserPoint.point());
        } finally {
            lock.unlock();
        }
        pointHistoryTable.insert(userId, amount, CHARGE, updateMillis);
        return chargedUserPoint;
    }
}
