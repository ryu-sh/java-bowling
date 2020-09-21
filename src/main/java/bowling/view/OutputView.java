package bowling.view;

import bowling.domain.Player;
import bowling.domain.frame.Frame;
import bowling.domain.score.Score;
import bowling.util.StringUtils;

import java.text.MessageFormat;
import java.util.stream.IntStream;

import static bowling.domain.frame.AbstractFrame.LAST_FRAME_NUMBER;

public class OutputView {

    public static final String EMPTY = "      |";

    public static void printBoard(Player player, Frame firstFrame) {
        System.out.println("| NAME |  01  |  02  |  03  |  04  |  05  |  06  |  07  |  08  |  09  |  10  |");
        printPins(player, firstFrame);
        System.out.println();
        printScore(firstFrame);
        System.out.println();
    }

    private static void printPins(Player player, Frame firstFrame) {
        System.out.print(MessageFormat.format("| {0} |", StringUtils.leftPad(player.getName(), 4)));
        Frame lastFrame = firstFrame;
        for (Frame nextFrame: firstFrame) {
            System.out.print(StringUtils.leftPad(nextFrame.toString(), 5) + " |");
            lastFrame = nextFrame;
        }
        printEmptyBoard(lastFrame);
    }

    private static void printScore(Frame firstFrame) {
        System.out.print("|      |");
        int totalScore = 0;
        Frame lastFrame = firstFrame;
        for (Frame nextFrame: firstFrame) {
            totalScore = printScore(totalScore, nextFrame);
            lastFrame = nextFrame;
        }
        printEmptyBoard(lastFrame);
    }

    private static int printScore(int totalScore, Frame nextFrame) {
        Score score = nextFrame.getScore();
        if (!score.isValid()) {
            System.out.print(EMPTY);
            return totalScore;
        }
        totalScore += score.getScore();
        System.out.print(StringUtils.leftPad(String.valueOf(totalScore), 5) + " |");
        return totalScore;
    }


    private static void printEmptyBoard(Frame lastFrame) {
        IntStream.range(lastFrame.getFrameNumber(), LAST_FRAME_NUMBER)
                .forEach(i -> System.out.print(StringUtils.leftPad("", 5) + " |"));
    }

}