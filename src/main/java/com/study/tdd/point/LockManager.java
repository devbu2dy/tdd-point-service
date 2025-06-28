package com.study.tdd.point;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockManager {

    private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public void lock(Long userId) {
        ReentrantLock lock = lockMap.computeIfAbsent(userId, k -> new ReentrantLock(true));
        lock.lock();
    }

    public void unlock(Long userId) {
        ReentrantLock lock = lockMap.get(userId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public void cleanUnusedLocks() {
        lockMap.entrySet().removeIf(entry ->
                !entry.getValue().isLocked() &&
                        entry.getValue().getQueueLength() == 0
        );
    }
}
