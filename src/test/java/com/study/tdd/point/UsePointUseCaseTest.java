package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.study.tdd.point.TransactionType.USE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UsePointUseCaseTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private UsePointUseCase sut;

    @Test
    void 포인트를_사용할_때_잔고가_50_000포인트이고_사용_포인트가_5_000포인트이면_잔고는_45_000포인트가_되고_사용_내역이_저장된다() {
        // given
        Long userId = 1L;
        Long balance = 50_000L;
        Long amount = 45_000L;
        TransactionType type = USE;
        Long updateMillis = System.currentTimeMillis();
        Long expectedBalance = balance - amount;

        given(userPointTable.selectById(userId))
                .willReturn(new UserPoint(1L, balance, System.currentTimeMillis()));

        // when
        UserPoint result = sut.use(userId, amount, updateMillis);

        // then
        assertThat(result.point()).isEqualTo(expectedBalance);
        assertThat(result.updateMillis()).isEqualTo(updateMillis);

        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1)).insertOrUpdate(userId, expectedBalance);
        verify(pointHistoryTable, times(1)).insert(userId, amount, type, updateMillis);
    }
}
