package com.study.tdd.spy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertTest {

    @Spy
    private Elvis elvis;

    @InjectMocks
    private Concert concert;

    @Test
    void spy_test() {
        // 실제 객체를 기반으로 하되, 특정 메서드만 모킹(mocking)할 수 있게 해주는 테스트 도구입니다.
        // Mock 객체와 달리 Spy는 stubbing 하지 않은 메서드들은 원래 객체의 실제 메서드가 실행됩니다.

        // given
        String lyrics = "Love me tender";
        String danceStyle = "ballroom";

        doReturn("Elegant ballroom dance!").when(elvis).dance(danceStyle); // Stubbing

//        when(elvis.dance(danceStyle)).thenReturn("Elegant ballroom dance!");

        // when
        String actualResult = concert.perform(lyrics, danceStyle);

        // then
        assertThat(actualResult).isEqualTo("Wow! Elvis is amazing! + Elegant ballroom dance!");

        verify(elvis, times(1)).sing(lyrics); // 실제 메서드 호출 검증
        verify(elvis, times(1)).dance(danceStyle); // stubbed 메서드 호출 검증
    }
}
