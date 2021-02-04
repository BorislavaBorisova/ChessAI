import java.util.Scanner;

public class Game {
    private Position currentPosition;
    private Scanner scanner;
    private boolean playersColor;

    private void initializePosition(){
        currentPosition = new Position(true, 0);
        for(int i = 0; i < 8; i++){
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

    public Game(){
        initializePosition();
        scanner = new Scanner(System.in);
    }

    public void play(){
        System.out.println("Who will be first player or computer?");
        String first = scanner.nextLine();
        playersColor = first.equals("player");
        currentPosition.print();
        if(playersColor) {
            while(!move()){};
            currentPosition.print();
        }
        while(!currentPosition.isTerminal()){
            currentPosition = currentPosition.minimaxDecision();
            currentPosition.print();
            if(currentPosition.isTerminal()) break;
            while(!move()){};
            currentPosition.print();
        }
    }

    public boolean move() {
        String line = scanner.nextLine();
        String[] args = line.split(" ");
        if (args.length == 1) {
            if (args[0].equals("O-O")) {
                currentPosition = currentPosition.castle(4, playersColor ? 0 : 7, 7, playersColor ? 0 : 7);
            } else if (args[0].equals("o-o")) {
                currentPosition = currentPosition.castle(4, playersColor ? 0 : 7, 7, playersColor ? 0 : 7);
            } else {
                System.out.println("Invalid move");
                return false;
            }
        } else if (args.length == 2) {
            String coord1 = args[0].toLowerCase(), coord2 = args[1].toLowerCase();
            currentPosition = currentPosition.move(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1');
        } else if (args.length == 3) {
            String coord1 = args[0].toLowerCase(), coord2 = args[1].toLowerCase(), coord3 = args[2].toLowerCase();
            switch (coord3) {
                case "queen":
                    currentPosition = currentPosition.promote(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1', new Queen(0, 0, playersColor));
                    break;
                case "bishop":
                    currentPosition = currentPosition.promote(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1', new Bishop(0, 0, playersColor));
                    break;
                case "rook":
                    currentPosition = currentPosition.promote(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1', new Rook(0, 0, playersColor));
                    break;
                case "knight":
                    currentPosition = currentPosition.promote(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1', new Knight(0, 0, playersColor));
                    break;
                default:
                    System.out.println("Invalid move");
                    return false;
            }
        } else if(args.length == 4 && args[2].toLowerCase().equals("en") && args[3].toLowerCase().equals("passant")){
            String coord1 = args[0].toLowerCase(), coord2 = args[1].toLowerCase();
            currentPosition = currentPosition.enPassant(coord1.charAt(0) - 'a', coord1.charAt(1) - '1', coord2.charAt(0) - 'a', coord2.charAt(1) - '1');
        } else {
            System.out.println("Invalid move");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        new Game().play();
    }
}
