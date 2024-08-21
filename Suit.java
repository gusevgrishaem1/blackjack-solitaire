public enum Suit {
    HEARTS('H'),
    DIAMONDS('D'),
    CLUBS('C'),
    SPADES('S');

    private final char value;

    Suit(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
