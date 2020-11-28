package bowling.domain.frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Frames {
    private static final int MAX_FRAME_NUMBER = 10;
    private final List<Frame> frames;
    private Integer currentFrameNumber;

    private Frames(List<Frame> frames) {
        this.frames = frames;
        this.currentFrameNumber = 1;
    }

    public static Frames create() {
        return new Frames(createFrames());
    }

    private static List<Frame> createFrames() {
        return Stream.iterate(Frame.first(), Frame::next)
                .limit(MAX_FRAME_NUMBER)
                .collect(Collectors.toList());
    }

    public int getCurrentFrameNumber() {
        return currentFrameNumber;
    }

    public boolean isFinished() {
        return currentFrameNumber > MAX_FRAME_NUMBER;
    }

    public void record(int score) {
        validateRecordPossible();
        Frame currentFrame = getFrame(currentFrameNumber);
        currentFrame.record(score);
        increaseCurrentFrameNumber(currentFrame);
    }

    private void validateRecordPossible() {
        if (isFinished()) {
            throw new InvalidFrameRecordActionException();
        }
    }

    private void increaseCurrentFrameNumber(Frame currentFrame) {
        if (frameIsFinished(currentFrame)) {
            currentFrameNumber += 1;
        }
    }

    private boolean frameIsFinished(Frame currentFrame) {
        return currentFrame.isFinished();
    }

    public List<Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }

    public List<Integer> calculateScores() {
        List<Integer> calculatedScores = new ArrayList<>();
        Integer previousFrameScore = 0;
        for (int frameNumber = 1; frameNumber <= MAX_FRAME_NUMBER; frameNumber++) {
            previousFrameScore = calculateScore(frameNumber, previousFrameScore);
            calculatedScores.add(previousFrameScore);
        }
        return calculatedScores;
    }

    private Integer calculateScore(int frameNumber, Integer previousFrameScore) {
        if (previousFrameScore == null || frameNumber > currentFrameNumber || !frameIsFinished(getFrame(frameNumber))) {
            return null;
        }
        return getFrame(frameNumber).calculateScore(previousFrameScore);
    }

    private Frame getFrame(int frameNumber) {
        return frames.get(frameNumber - 1);
    }
}
