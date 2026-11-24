package defult;

import java.util.Scanner;

public class Game {
    private Player player1;
    private Player player2;
    private Board board1;
    private Board board2;
    private Player currentPlayer;
    private int totalTurns;

    // New game constructor
    public Game() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Player 1 name: ");
        player1 = new Player(sc.nextLine().trim());
        System.out.print("Enter Player 2 name: ");
        player2 = new Player(sc.nextLine().trim());

        board1 = new Board(10, 10);
        board2 = new Board(10, 10);
        board1.placeCreatures();
        board2.placeCreatures();

        currentPlayer = player1;
        this.totalTurns = 0;
    }

    // Resume game constructor with turn tracking
    public Game(Player p1, Player p2, String currentName, Board b1, Board b2, int totalTurns) {
        this.player1 = p1;
        this.player2 = p2;
        this.board1 = b1;
        this.board2 = b2;
        this.currentPlayer = currentName.equals(p1.getName()) ? p1 : p2;
        this.totalTurns = totalTurns;
    }
    
    // Fallback constructor
    public Game(Player p1, Player p2, String currentName, Board b1, Board b2) {
        this(p1, p2, currentName, b1, b2, 0); 
    }

    // Main game loop
    public void play() {
        Scanner sc = new Scanner(System.in);
        boolean gameOver = false;
        
        // This flag controls returning to the Main Menu (Option 4: Exit in Main.java)
        boolean exitToMainMenu = false; 

        while (!gameOver && !exitToMainMenu) {
            System.out.println("\n--- " + currentPlayer.getName() + "'s Turn (Total Turns: " + totalTurns + ") ---");
            Board currentBoard = (currentPlayer == player1) ? board1 : board2;
            
            // --- Turn Menu Loop ---
            boolean turnOver = false;
            while (!turnOver) {
                currentBoard.display();
                System.out.println("Score: " + currentPlayer.getScore());
                System.out.println("Health: " + currentPlayer.getHealth());
                
                System.out.println("\n--- Action Menu ---");
                System.out.println("1. Guess Coordinate (Continue Turn)");
                System.out.println("2. Save Game and Quit");
                System.out.println("3. Exit Game (Unsaved changes will be lost)");
                System.out.print("Choose action (1-3): ");
                
                String input = sc.nextLine().trim();
                int action = -1;
                try {
                    action = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                    continue;
                }
                
                switch (action) {
                    case 1:
                        // Continue Turn (Guess Coordinate)
                        int r = getValidInput(sc, "Enter row (1-10): ");
                        int c = getValidInput(sc, "Enter col (1-10): ");

                        boolean guessed = currentBoard.guess(r, c, currentPlayer);

                        if (guessed) {
                            totalTurns++; // Increment total turns only for a valid guess

                            // Hint System Check
                            if (totalTurns > 0 && totalTurns % 10 == 0) {
                                System.out.println("\n*** HINT OPPORTUNITY! ***");
                                System.out.print("A hint is available! Reveal an unrevealed creature location on your board? (y/n): ");
                                String hintAns = sc.nextLine().trim();
                                
                                if (hintAns.equalsIgnoreCase("y")) {
                                    currentBoard.revealOneCreatureHint(currentPlayer); 
                                } else {
                                    System.out.println("Hint rejected.");
                                }
                            }
                        }
                        turnOver = true; // End the current player's action phase
                        break;
                        
                    case 2:
                        // Save Game and Quit
                        SaveLoadManager.saveGame(this, "save.txt");
                        System.out.println("Game saved. Returning to Main Menu.");
                        turnOver = true;
                        exitToMainMenu = true; // Flag to exit the main game loop
                        break;
                        
                    case 3:
                        // Exit Game without Saving
                        System.out.println("Exiting without saving. Returning to Main Menu.");
                        turnOver = true;
                        exitToMainMenu = true; // Flag to exit the main game loop
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                        break;
                }
            } // End of Turn Menu Loop

            // Check if game over conditions were met during the turn, 
            // but skip if we are already exiting to the main menu.
            if (!exitToMainMenu) {
                boolean creaturesDone = board1.allCreaturesFound() || board2.allCreaturesFound();
                boolean healthZero = player1.getHealth() <= 0 || player2.getHealth() <= 0;

                if (creaturesDone || healthZero) {
                    gameOver = true;
                    System.out.println("Game Over!");
                    System.out.println(player1.getName() + " -> Score: " + player1.getScore() + " | Health: " + player1.getHealth());
                    System.out.println(player2.getName() + " -> Score: " + player2.getScore() + " | Health: " + player2.getHealth());
                    if (player1.getScore() > player2.getScore()) {
                        System.out.println("Winner: " + player1.getName());
                    } else if (player2.getScore() > player1.getScore()) {
                        System.out.println("Winner: " + player2.getName());
                    } else {
                        System.out.println("It's a tie!");
                    }
                } else {
                    currentPlayer = (currentPlayer == player1) ? player2 : player1; // Switch player
                }
            }
        }
        
        // If the game loop was broken by a quit/exit command, 
        // the Main class loop handles returning to the menu.
    }

    // Input validation loop (unchanged)
    private int getValidInput(Scanner sc, String prompt) {
        int val = -1;
        while (val < 1 || val > 10) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                val = Integer.parseInt(input);
                if (val < 1 || val > 10) {
                    System.out.println("Invalid! Please enter between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
        return val;
    }

    // Getters for SaveLoadManager (unchanged)
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Board getBoard1() { return board1; }
    public Board getBoard2() { return board2; }
    public int getTotalTurns() { return totalTurns; }
}