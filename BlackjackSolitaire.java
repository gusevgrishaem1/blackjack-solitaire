import java.util.Scanner;

public class BlackjackSolitaire {
    public void runGame() {
        Deck deck = new Deck();
        Scanner scanner = new Scanner(System.in);
        deck.displayField();
        while (!deck.isEnd()) {
            deck.readAndPutCard(scanner);
            deck.displayField();
        }

        System.out.println("Баллы: " + deck.calculateScores());
        System.out.println("Игра окончена!");
        scanner.close();
    }
}
