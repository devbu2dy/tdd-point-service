package com.study.tdd.point;

import com.study.tdd.database.PointHistoryTable;
import com.study.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.study.tdd.point.TransactionType.CHARGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChargePointUseCaseTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private ChargePointUseCase sut;

    @Test
    void 포인트를_충전할_때_1_000_000포인트를_충전하면_잔고는_5_000_000포인트가_되고_충전_내역이_저장된다() {
        // given
        Long userId = 1L;
        Long balance = 4_000_000L;
        Long amount = 1_000_000L;
        TransactionType type = CHARGE;
        Long updateMillis = System.currentTimeMillis();
        Long totalAmount = balance + amount;

        given(userPointTable.selectById(userId))
                .willReturn(new UserPoint(userId, balance, System.currentTimeMillis()));

        // when
        UserPoint result = sut.charge(userId, amount, updateMillis);

        // then
        assertThat(result.point()).isEqualTo(totalAmount);
        assertThat(result.updateMillis()).isEqualTo(updateMillis);

        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1)).insertOrUpdate(userId, totalAmount);
        verify(pointHistoryTable, times(1)).insert(userId, amount, type, updateMillis);
    }
}
