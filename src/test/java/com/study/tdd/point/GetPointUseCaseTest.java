package com.study.tdd.point;

import com.study.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GetPointUseCaseTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private GetPointUseCase sut;

    @Test
    void 포인트를_조회할_때_포인트_정보가_없으면_0포인트를_반환한다() {
        // given
        Long userId = 1L;

        given(userPointTable.selectById(userId)).willReturn(UserPoint.empty(userId));

        // when
        UserPoint result = sut.findById(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);

        verify(userPointTable, times(1)).selectById(userId);
    }

    @Test
    void 포인트를_조회할_때_포인트_정보가_있으면_포인트를_반환한다() {
        // given
        Long userId = 1L;
        Long balance = 10_000L;
        Long updateMillis = System.currentTimeMillis();

        given(userPointTable.selectById(userId)).willReturn(new UserPoint(userId, balance, updateMillis));

        // when
        UserPoint result = sut.findById(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(balance);
        assertThat(result.updateMillis()).isEqualTo(updateMillis);

        verify(userPointTable, times(1)).selectById(userId);
    }
}
