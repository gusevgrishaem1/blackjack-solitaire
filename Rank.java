public enum Rank {
    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"),
    FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"),
    EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"),
    JACK(10, "J"), QUEEN(10, "Q"),
    KING(10, "K"), ACE(11, "A");

    private final int score;
    private final String value;

    Rank(int score, String value) {
        this.score = score;
        this.value = value;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return value;
    }
}
