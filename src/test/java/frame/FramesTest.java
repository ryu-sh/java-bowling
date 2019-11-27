package frame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import score.ScoreInfo;
import score.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FramesTest {

    private static Stream<Arguments> getFrames() {
        return Stream.of(
                Arguments.of(new ArrayList<>(), 1),
                Arguments.of(Arrays.asList(aMockNormalFrame(), aMockNormalFrame()), 2)
        );
    }

    private static NormalFrame aMockNormalFrame() {
        return new NormalFrame(1, new ArrayList<>());
    }


    @ParameterizedTest
    @MethodSource("getFrames")
    void getNextFrameNumber(List<Frame> allFrames, int number) {
        Frames frames = new Frames(allFrames);
        assertThat(frames.getNowFrameNumber()).isEqualTo(number);
    }

    @Test
    void getNowFrame() {
        Frames frames = new Frames(new ArrayList<>());
        Frame nowFrame = frames.getNowFrame();

        assertThat(nowFrame).isEqualTo(new NormalFrame(1, new ArrayList<>()));

        nowFrame.bowling(1);
        nowFrame = frames.getNowFrame();

        assertThat(nowFrame).isEqualTo(new NormalFrame(1, Arrays.asList(ScoreInfo.firstScore(1))));

        nowFrame.bowling(1);
        nowFrame = frames.getNowFrame();

        assertThat(nowFrame).isEqualTo(new NormalFrame(2, new ArrayList<>()));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,false", "9,false", "10,true"})
    void reachLast(int size, boolean answer) {
        List<Frame> allFrame = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            allFrame.add(NormalFrame.firstNormalFrame());
        }

        Frames frames = new Frames(allFrame);
        assertThat(frames.reachLast()).isEqualTo(answer);
        assertThat(frames.isNotLast()).isNotEqualTo(answer);
    }

    @Test
    void size() {
        List<Frame> allFrame = new ArrayList<>();
        Frames frames = new Frames(allFrame);
        assertThat(frames.size()).isEqualTo(0);

        allFrame.add(NormalFrame.firstNormalFrame());
        assertThat(frames.size()).isEqualTo(1);
    }

    @Test
    void getLastFrame() {
        List<Frame> allFrame = new ArrayList<>();
        Frames frames = new Frames(allFrame);

        assertThat(frames.getLastFrame()).isEqualTo(LastFrame.init());
    }

    @Test
    void findScoreInfos() {
        Frames frames = new Frames(new ArrayList<>());

        assertThat(frames.findScoreInfos(0)).isEqualTo(new ArrayList<>());

        NormalFrame normalFrame = NormalFrame.firstNormalFrame();
        normalFrame.bowling(1);
        frames = new Frames(Arrays.asList(normalFrame));

        assertThat(frames.findScoreInfos(0)).isEqualTo(Arrays.asList(new ScoreInfo(1, Status.MISS)));
    }
}