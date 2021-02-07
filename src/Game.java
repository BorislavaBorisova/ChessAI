import java.util.Random;
import java.util.Scanner;

public class Game {
    private Position currentPosition;
    private Scanner scanner;
    private boolean playersColor;
    private int[][] table;
    private int[] hashes;
    private int whiteTurn, blackTurn;

    private void initializePosition() {
        currentPosition = new Position(true, 0);
        for (int i = 0; i < 8; i++) {
            currentPosition.placePiece(new Pawn(i, 1, true));
            currentPosition.placePiece(new Pawn(i, 6, false));
        }
        currentPosition.placePiece(new Rook(0, 0, true));
        currentPosition.placePiece(new Rook(0, 7, false));
        currentPosition.placePiece(new Knight(1, 0, true));
        currentPosition.placePiece(new Knight(1, 7, false));
        currentPosition.placePiece(new Bishop(2, 0, true));
        currentPosition.placePiece(new Bishop(2, 7, false));
        currentPosition.placePiece(new Queen(3, 0, true));
        currentPosition.placePiece(new Queen(3, 7, false));
        currentPosition.placePiece(new King(4, 0, true));
        currentPosition.placePiece(new King(4, 7, false));
        currentPosition.placePiece(new Bishop(5, 0, true));
        currentPosition.placePiece(new Bishop(5, 7, false));
        currentPosition.placePiece(new Knight(6, 0, true));
        currentPosition.placePiece(new Knight(6, 7, false));
        currentPosition.placePiece(new Rook(7, 0, true));
        currentPosition.placePiece(new Rook(7, 7, false));
    }

    private void initializeHash(Random random) {
        table = new int[64][18];
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 18; j++) {
                table[i][j] = random.nextInt(Integer.MAX_VALUE);
            }
        }
    }

    private void initializeTurn(Random random) {
        whiteTurn = random.nextInt(Integer.MAX_VALUE);
        blackTurn = random.nextInt(Integer.MAX_VALUE);
    }

    public Game() {
        initializePosition();
        Random random = new Random();
        initializeHash(random);
        initializeTurn(random);
        scanner = new Scanner(System.in);
    }

    public void play() {
        System.out.println("Who will be first player or computer?");
        String first = scanner.nextLine();
        playersColor = first.equals("player");
        currentPosition.print();
        if (playersColor) {
            while (!move()) {
            }
            ;
            currentPosition.print();
        }
        while (!currentPosition.isTerminal()) {
            currentPosition = currentPosition.minimaxDecision(!playersColor);
            currentPosition.print();
            if (currentPosition.isTerminal())
                break;
            while (!move()) {
            }
            ;
            currentPosition.print();
        }
    }

    public boolean move() {
        String line = scanner.nextLine();
        String[] args = line.split(" ");
        Move move;
        if (args.length == 1) {
            if (args[0].equals("O-O")) {
                move = new Move(currentPosition.getPiece(4, playersColor ? 0 : 7).clone(), 2, playersColor ? 0 : 7,
                        null,
                        (Rook) currentPosition.getPiece(0, playersColor ? 0 : 7).clone(), null);
            } else if (args[0].equals("o-o")) {
                move = new Move(currentPosition.getPiece(4, playersColor ? 0 : 7).clone(), 6, playersColor ? 0 : 7,
                        null,
                        (Rook) currentPosition.getPiece(7, playersColor ? 0 : 7).clone(), null);
            } else {
                System.out.println("Invalid move");
                return false;
            }
        } else {
            String coord1 = args[0].toLowerCase(), coord2 = args[1].toLowerCase();
            int x1 = coord1.charAt(0) - 'a', y1 = coord1.charAt(1) - '1',
                    x2 = coord2.charAt(0) - 'a', y2 = coord2.charAt(1) - '1';

            if (args.length == 2) {
                move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y2),
                        null, null);
            } else if (args.length == 3) {
                String coord3 = args[2].toLowerCase();
                switch (coord3) {
                case "queen":
                    move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y2),
                            null,
                            new Queen(0, 0, playersColor));
                    break;
                case "bishop":
                    move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y2),
                            null,
                            new Bishop(0, 0, playersColor));
                    break;
                case "rook":
                    move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y2),
                            null,
                            new Rook(0, 0, playersColor));
                    break;
                case "knight":
                    move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y2),
                            null,
                            new Knight(0, 0, playersColor));
                    break;
                default:
                    System.out.println("Invalid move");
                    return false;
                }
            } else if (args.length == 4 && args[2].toLowerCase().equals("en")
                    && args[3].toLowerCase().equals("passant")) {
                move = new Move(currentPosition.getPiece(x1, y1).clone(), x2, y2, currentPosition.getPiece(x2, y1),
                        null, null);

            } else {
                System.out.println("Invalid move");
                return false;
            }
        }

        currentPosition = currentPosition.move(move);
        return true;
    }

    public static void main(String[] args) {
        new Game().play();
    }
}
