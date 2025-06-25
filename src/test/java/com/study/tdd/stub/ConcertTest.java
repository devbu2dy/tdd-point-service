package com.study.tdd.stub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertTest {

    @Mock
    private Singer mockSinger;

    @InjectMocks
    private Concert concert;

    @Test
    void stub_test() {
        // given
        String lyrics = "I'll have a blue~ Christmas without you~";
        String expectedCheer = "I love you Elvis~";

        when(mockSinger.sing(lyrics)).thenReturn(expectedCheer);

        // when
        String actualResult = concert.perform(lyrics); // Stubbing

        // then
        assertThat(actualResult).isEqualTo(expectedCheer); // 상태 검증 (Stub)
        verify(mockSinger, times(1)).sing(lyrics); // 행위 검증 (Mock)
    }

    @Test
    void stub_test_with_fixture() {
        // given
        Singer elvisStub = new ElvisStub();
        Concert concertWithStub = new Concert(elvisStub);
        String lyrics = "Love me tender, love me sweet";

        // when
        String actualResult = concertWithStub.perform(lyrics);

        // then
        assertThat(actualResult).isEqualTo("I love you Elvis!");
    }

    @Test
    void stub_test_with_bdd_mockito() {
        // given
        String lyrics = "I'll have a blue~ Christmas without you~";
        String expectedCheer = "I love you Elvis~";

        BDDMockito.given(mockSinger.sing(lyrics)).willReturn(expectedCheer);

        // when
        String actualResult = concert.perform(lyrics);

        // then
        assertThat(actualResult).isEqualTo(expectedCheer);
        verify(mockSinger, times(1)).sing(lyrics);
    }
}
