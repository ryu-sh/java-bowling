package bowling.domain;

import bowling.domain.rollresult.OneHit;
import bowling.domain.rollresult.RollResultType;

import java.util.Objects;

public class Pin {
    private static final String INVALID_PIN_TRY = "핀을 쓰러뜨릴 수 있는 횟수는 0~3회입니다.";
    private static final String INVALID_PIN_NUM = "핀의 수는 0~10개입니다.";
    private static final int PIN_TRY_LOWER_BOUND = 0;
    private static final int PIN_TRY_UPPER_BOUND = 3;
    private static final int PIN_NUM_LOWER_BOUND = 0;
    private static final int PIN_NUM_UPPER_BOUND = 10;

    private int tryNum = 0;
    private int pinNum = 10;

    private Pin() {

    }

    private Pin(int tryNum) {
        this.tryNum = tryNum;
    }

    private Pin(int tryNum, int pinNum) {
        this.tryNum = tryNum;
        this.pinNum = pinNum;
    }

    public static Pin of() {
        return new Pin();
    }

    public static Pin reload(Pin pin) {
        validTry(pin.tryNum);
        return new Pin(pin.tryNum);
    }

    public static Pin of(int tryNum, int pinNum) {
        validTry(tryNum);
        validPinNum(pinNum);
        return new Pin(tryNum, pinNum);
    }

    private static void validTry(int tryNum) {
        if (tryNum < PIN_TRY_LOWER_BOUND || tryNum > PIN_TRY_UPPER_BOUND) {
            throw new IllegalArgumentException(INVALID_PIN_TRY);
        }
    }

    private static void validPinNum(int pinNum) {
        if (pinNum < PIN_NUM_LOWER_BOUND || pinNum > PIN_NUM_UPPER_BOUND) {
            throw new IllegalArgumentException(INVALID_PIN_NUM);
        }
    }

    private void fallen(HitNumber rollNumber) {
        tryNum++;
        pinNum = rollNumber.hit(pinNum);
    }

    public RollResultType firstHit(HitNumber rollNumber) {
        fallen(rollNumber);
        return OneHit.of(PIN_NUM_UPPER_BOUND - pinNum);
    }

    public RollResultType nextHit(RollResultType type, HitNumber rollNumber) {
        validTry(tryNum);
        validPinNum(pinNum);
        fallen(rollNumber);
        return type.next(PIN_NUM_UPPER_BOUND - pinNum);
    }

    public boolean isLast() {
        return tryNum == PIN_TRY_UPPER_BOUND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return tryNum == pin.tryNum && pinNum == pin.pinNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tryNum, pinNum);
    }

}
