package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPointHistoryUseCase {

    private final PointHistoryTable pointHistoryTable;

    public List<PointHistory> selectAllByUserId(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
