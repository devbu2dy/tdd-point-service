package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.study.tdd.point.TransactionType.CHARGE;
import static com.study.tdd.point.TransactionType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetPointHistoryUseCaseTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private GetPointHistoryUseCase sut;

    @Test
    void 특정_사용자의_포인트_내역을_조회할_때_내역이_없으면_빈_목록을_반환한다() {
        // given
        Long userId = 1L;

        given(pointHistoryTable.selectAllByUserId(userId)).willReturn(List.of());

        // when
        List<PointHistory> result = sut.selectAllByUserId(userId);

        // then
        assertThat(result).hasSize(0);

        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
    }

    @Test
    void 특정_사용자의_포인트_내역을_조회할_때_내역이_존재하면_내역_목록을_반환한다() {
        // given
        Long userId = 1L;

        given(pointHistoryTable.selectAllByUserId(userId))
                .willReturn(List.of(
                        new PointHistory(1L, userId, 5000L, CHARGE, System.currentTimeMillis()),
                        new PointHistory(2L, userId, 10_000L, CHARGE, System.currentTimeMillis()),
                        new PointHistory(3L, userId, 3000L, USE, System.currentTimeMillis())
                ));

        // when
        List<PointHistory> result = sut.selectAllByUserId(userId);

        // then
        assertThat(result).hasSize(3)
                .extracting("id", "userId", "amount", "type")
                .containsExactlyInAnyOrder(
                        tuple(1L, userId, 5000L, CHARGE),
                        tuple(2L, userId, 10_000L, CHARGE),
                        tuple(3L, userId, 3000L, USE)
                );

        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
    }
}
