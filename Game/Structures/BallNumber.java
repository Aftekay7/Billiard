package Game.Structures;

public enum BallNumber {
    WHITE,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    HALF_NINE,
    HALF_TEN,
    HALF_ELEVEN,
    HALF_TWELVE,
    HALF_THIRTEEN,
    HALF_FOURTEEN,
    HALF_FIFTEEN;

    public boolean isHalf() {
        String s = this.toString();
        return s.contains("HALF");
    }
}
