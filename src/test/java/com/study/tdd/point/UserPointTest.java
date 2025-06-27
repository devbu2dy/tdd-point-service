package com.study.tdd.point;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.study.tdd.point.PointPolicy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserPointTest {

    private UserPoint sut;

    @Nested
    class 포인트_충전_유효성_검사_실패 {

        @Test
        void 포인트를_충전할_때_최소_충전_금액_미만이면_IllegalArgumentException을_반환한다() {
            // given
            sut = UserPoint.empty(1L);

            Long amount = 9_000L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.charge(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 최소 " + MIN_CHARGE_AMOUNT.formattedValue() + " 포인트 이상부터 충전할 수 있습니다.");
        }

        @Test
        void 포인트를_충전할_때_충전_단위_맞지_않으면_IllegalArgumentException을_반환한다() {
            // given
            sut = UserPoint.empty(1L);

            Long amount = 10_001L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.charge(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트 충전은 " + CHARGE_UNIT.formattedValue() + " 포인트 단위로만 가능합니다.");
        }

        @Test
        void 포인트를_충전할_때_1회_충전_한도를_초과하면_IllegalArgumentException을_반환한다() {
            // given
            sut = UserPoint.empty(1L);

            Long amount = 1_001_000L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.charge(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 최대 " + MAX_CHARGE_PER_OPERATION.formattedValue() + " 포인트까지 한 번에 충전할 수 있습니다.");
        }

        @Test
        void 포인트를_충전할_때_최대_보유_한도를_초과하면_IllegalArgumentException을_반환한다() {
            // given
            sut = new UserPoint(1L, 5_000_000L, System.currentTimeMillis());

            Long amount = 10_000L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.charge(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트 잔고가 최대 한도(" + MAX_BALANCE.formattedValue() + " 포인트)를 초과하여 충전이 불가합니다.");
        }
    }

    @ParameterizedTest(name = "[{index}] balance={0}, amount={1}, expectedMessage={2}")
    @MethodSource("provideInvalidChargeParameters")
    void 포인트_충전_유효성_검사_실패(
            Long balance,
            Long amount,
            String expectedMessage
    ) {
        // given
        sut = new UserPoint(1L, balance, System.currentTimeMillis());

        Long updateMillis = System.currentTimeMillis();

        // when & then
        assertThatThrownBy(() -> sut.charge(amount, updateMillis))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    private static Stream<Arguments> provideInvalidChargeParameters() {
        return Stream.of(
                Arguments.of(0L, 9_000L, "포인트는 최소 " + MIN_CHARGE_AMOUNT.formattedValue() + " 포인트 이상부터 충전할 수 있습니다."),
                Arguments.of(0L, 10_001L, "포인트 충전은 " + CHARGE_UNIT.formattedValue() + " 포인트 단위로만 가능합니다."),
                Arguments.of(0L, 1_001_000L, "포인트는 최대 " + MAX_CHARGE_PER_OPERATION.formattedValue() + " 포인트까지 한 번에 충전할 수 있습니다."),
                Arguments.of(5_000_000L, 10_000L, "포인트 잔고가 최대 한도(" + MAX_BALANCE.formattedValue() + " 포인트)를 초과하여 충전이 불가합니다.")
        );
    }

    @ParameterizedTest(name = "[{index}] balance={0}, amount={1}, expectedTotal={2}")
    @MethodSource("provideValidChargeParameters")
    void 포인트_충전_성공(
            Long balance,
            Long amount,
            Long expectedTotal
    ) {
        // given
        sut = new UserPoint(1L, balance, System.currentTimeMillis());

        Long updateMillis = System.currentTimeMillis();

        // when
        UserPoint result = sut.charge(amount, updateMillis);

        // then
        assertThat(result.point()).isEqualTo(expectedTotal);
        assertThat(result.updateMillis()).isEqualTo(updateMillis);
    }

    private static Stream<Arguments> provideValidChargeParameters() {
        return Stream.of(
                Arguments.of(0L, 10_000L, 10_000L),           // 기본 충전
                Arguments.of(40_000L, 10_000L, 50_000L),      // 추가 충전
                Arguments.of(0L, 1_000_000L, 1_000_000L),     // 최대 금액 충전
                Arguments.of(4_990_000L, 10_000L, 5_000_000L) // 최대 잔고 도달
        );
    }

    @Nested
    class 포인트_사용_유효성_검사_실패 {

        @Test
        void 포인트를_사용할_때_음수_또는_0이면_IllegalArgumentException을_반환한다() {
            // given
            sut =  UserPoint.empty(1L);

            Long amount = 0L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.use(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용 포인트는 0보다 커야 합니다.");
        }

        @Test
        void 포인트를_사용할_때_최소_사용_금액_미만이면_IllegalArgumentException을_반환한다() {
            // given
            sut =  UserPoint.empty(1L);

            Long amount = 4999L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.use(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("포인트는 최소 " + MIN_USE_AMOUNT.formattedValue() + " 포인트 이상부터 사용할 수 있습니다.");
        }

        @Test
        void 포인트를_사용할_때_잔고가_부족하면_IllegalArgumentException을_반환한다() {
            // given
            Long balance = 4999L;
            sut =  new UserPoint(1L, balance, System.currentTimeMillis());

            Long amount = 5000L;
            Long updateMillis = System.currentTimeMillis();

            // when & then
            assertThatThrownBy(() -> sut.use(amount, updateMillis))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잔고가 부족합니다. (현재 잔고: %,d 포인트, 사용 요청: %,d 포인트)", balance, amount);
        }
    }

    @Nested
    class 포인트_사용_성공 {

        @Test
        void 포인트를_사용할_때_잔고가_5000포인트이고_사용_포인트가_5000포인트이면_잔고는_0포인트가_된다() {
            // given
            Long balance = 5000L;
            sut =  new UserPoint(1L, balance, System.currentTimeMillis());

            Long amount = 5000L;
            Long updateMillis = System.currentTimeMillis();

            // when
            UserPoint result = sut.use(amount, updateMillis);

            // then
            assertThat(result.point()).isEqualTo(balance - amount);
            assertThat(result.updateMillis()).isEqualTo(updateMillis);
        }
    }
}
