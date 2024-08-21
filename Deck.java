import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Deck {
    private final Field[][] fields;
    private final Field[] trash;
    private final Stack<Card> deck;

    public Deck() {
        this.fields = new Field[][]{new Field[]{new Field(1), new Field(2), new Field(3), new Field(4), new Field(5)}, new Field[]{new Field(6), new Field(7), new Field(8), new Field(9), new Field(10)}, new Field[]{new Field(11), new Field(12), new Field(13), null, null}, new Field[]{new Field(14), new Field(15), new Field(16), null, null}};
        this.trash = new Field[4];
        this.deck = new Stack<>();
        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                deck.push(new Card(rank, suit));
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void readAndPutCard(Scanner scanner) {
        if (deck.isEmpty()) {
            System.out.println("Колода карт пуста.");
            return;
        }

        Card card = deck.pop();
        System.out.println("Выпала карта: " + card);

        int cellNumber = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Введите номер ячейки (1-16) для размещения карты или 17-20 для сброса: ");
            if (scanner.hasNextInt()) {
                cellNumber = scanner.nextInt();
                if (cellNumber >= 1 && cellNumber <= 20) {
                    validInput = true;
                } else {
                    System.out.println("Ошибка: введите число от 1 до 20.");
                }
            } else {
                System.out.println("Ошибка: введите целое число.");
                scanner.next();
            }
        }

        if (cellNumber <= 16) {
            if (cellNumber >= 14) {
                cellNumber += 2;
            }
            int row = (cellNumber - 1) / 5;
            int col = (cellNumber - 1) % 5;
            try {
                addCard(row, col, card);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else {
            addCardToWaste(cellNumber - 17, card);
        }
    }


    public void addCard(int row, int col, Card card) {
        if (row >= 0 && row < this.fields.length && col >= 0 && col < this.fields[row].length) {
            if (this.fields[row][col].getCard() != null) {
                throw new IllegalArgumentException("Ячейка уже занята.");
            }
            this.fields[row][col].setCard(card);
        } else {
            throw new IllegalArgumentException("Некорректное положение.");
        }
    }

    public void addCardToWaste(int column, Card card) {
        if (column >= 0 && column < 4) {
            Field val = trash[column];
            if (val != null) {
                System.out.println("Эта мусорная ячейка занята");
            } else {
                trash[column] = new Field(column);
                trash[column].setCard(card);
            }
        } else {
            throw new IllegalArgumentException("Некорректный номер мусорной колонки.");
        }
    }


    public void displayField() {
        System.out.println("Текущее состояние игрового поля:");
        for (Field[] field : this.fields) {
            for (Field value : field) {
                if (value == null) {
                    continue;
                }
                if (value.getCard() != null) {
                    System.out.print(value.getCard().toString() + "\t");
                } else {
                    System.out.print(value.getNum() + "\t");
                }
            }
            System.out.println();
        }

        System.out.println("Мусорные колонки:");
        for (int i = 0; i < trash.length; i++) {
            if (trash[i] == null) {
                continue;
            }
            System.out.print("Колонка " + (i + 1) + ": ");
            System.out.print(trash[i].getCard().toString() + "\t");
        }
        System.out.println();
    }

    public boolean isEnd() {
        for (Field[] field : this.fields) {
            for (Field value : field) {
                if (value == null) {
                    continue;
                }
                if (value.getCard() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public int calculateScores() {
        int totalHorizontalSum = getTotalHorizontalSum();
        int totalVerticalSum = getTotalVerticalSum();
        return totalHorizontalSum + totalVerticalSum;
    }

    private int getTotalHorizontalSum() {
        int totalHorizontalSum = 0;
        for (Field[] field : fields) {
            int horizontalSum = 0;
            int aceCount = 0;
            int cardCount = 0;
            int limit = 21;
            int step = 10;

            for (Field value : field) {
                if (value == null || value.getCard() == null) {
                    continue;
                }
                if (value.getCard().getRank().equals(Rank.ACE)) {
                    aceCount++;
                }
                horizontalSum += value.getCard().getRank().getScore();
                cardCount++;
            }

            if (horizontalSum > limit) {
                while (aceCount != 0 && horizontalSum > limit) {
                    horizontalSum -= step;
                    aceCount--;
                }
                if (horizontalSum > limit) {
                    horizontalSum = 0; // Перебор, обнуляем очки
                }
            }

            totalHorizontalSum += assignPoints(horizontalSum, cardCount);
        }
        return totalHorizontalSum;
    }

    private int getTotalVerticalSum() {
        int totalVerticalSum = 0;
        for (int j = 0; j < 5; j++) {
            int verticalSum = getVerticalSum(j);
            totalVerticalSum += verticalSum;
        }
        return totalVerticalSum;
    }

    private int getVerticalSum(int j) {
        int verticalSum = 0;
        int aceCount = 0;
        int cardCount = 0;
        int limit = 21;
        int step = 10;

        for (Field[] field : fields) {
            if (field[j] == null || field[j].getCard() == null) {
                continue;
            }
            if (field[j].getCard().getRank().equals(Rank.ACE)) {
                aceCount++;
            }
            verticalSum += field[j].getCard().getRank().getScore();
            cardCount++;
        }

        if (verticalSum > limit) {
            while (aceCount != 0 && verticalSum > limit) {
                verticalSum -= step;
                aceCount--;
            }
            if (verticalSum > limit) {
                verticalSum = 0; // Перебор, обнуляем очки
            }
        }

        return assignPoints(verticalSum, cardCount);
    }

    private int assignPoints(int sum, int cardCount) {
        if (sum == 21 && cardCount == 2) {
            return 10;
        } else if (sum == 21) {
            return 7;
        } else if (sum == 20) {
            return 5;
        } else if (sum == 19) {
            return 4;
        } else if (sum == 18) {
            return 3;
        } else if (sum == 17) {
            return 2;
        } else if (sum <= 16) {
            return 1;
        } else {
            return 0;
        }
    }
}
