public class Field {
    private final int num;
    private Card card;

    public Field(int num) {
        this.num = num;
        this.card = null;
    }

    public int getNum() {
        return num;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
