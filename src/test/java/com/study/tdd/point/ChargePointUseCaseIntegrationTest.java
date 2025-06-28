package com.study.tdd.point;

import com.study.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChargePointUseCaseIntegrationTest {

    @Autowired
    private ChargePointUseCase sut;

    @Autowired
    private UserPointTable userPointTable;

    @BeforeEach
    public void setup() {
        userPointTable.insertOrUpdate(1L, 0L);
    }

    @Test
    void 동시에_10_000포인트_충전_요청이_5건_들어오면_총_50_000포인트가_충전된다() throws InterruptedException {
        // given
        Long userId = 1L;
        Long amount = 10_000L;
        int threadCount = 5;
        Long totalAmount = amount * threadCount;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        IntStream.range(0, threadCount).forEach(i -> executorService.submit(() -> {
            try {
                sut.charge(userId, amount, System.currentTimeMillis());
            } catch (Exception e) {
                throw new RuntimeException("포인트 충전 실패", e);
            } finally {
                latch.countDown();
            }
        }));

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        UserPoint result = userPointTable.selectById(userId);
        assertThat(result.point()).isEqualTo(totalAmount);
    }
}
