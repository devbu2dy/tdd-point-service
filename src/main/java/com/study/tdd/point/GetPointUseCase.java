package com.study.tdd.point;

import com.study.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPointUseCase {

    private final UserPointTable userPointTable;

    public UserPoint findById(Long userId) {
        return userPointTable.selectById(userId);
    }
}
