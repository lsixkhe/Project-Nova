package defult;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Game game = null;
        boolean running = true;

        while (running) {
            int choice = -1;
            while (choice < 1 || choice > 4) {
                System.out.println("\n=== Project Nova ===");
                System.out.println("1. Start New Game");
                System.out.println("2. Resume Previous Game");
                System.out.println("3. Help");
                System.out.println("4. Exit");
                System.out.print("Choose option (1-4): ");

                String input = sc.nextLine().trim();
                try {
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > 4) {
                        System.out.println("Invalid choice! Please enter 1–4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number between 1 and 4.");
                }
            }

            switch (choice) {
                case 1:
                    game = new Game();
                    game.play();
                    break;
                case 2:
                    game = SaveLoadManager.loadGame("save.txt");
                    if (game != null) {
                        System.out.println("Resuming saved game...");
                        game.play();
                    } else {
                        System.out.println("No saved game found.");
                    }
                    break;
                case 3:
                    System.out.println("Help:");
                    System.out.println("- Grid is 10x10. Coordinates start at (1,1).");
                    System.out.println("- Creatures: +5 points. Booby traps: -2 points, -10 health.");
                    System.out.println("- Power-ups: +2 points, +10 health.");
                    System.out.println("- You can save during your turn and resume later.");
                    break;
                case 4:
                    running = false;
                    break;
            }
        }
        System.out.println("Goodbye!");
    }
}
