package defult;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class SaveLoadManager {

    /**
     * Saves the current Game state (Players, Current Player, and Boards) to a file. 
     */
    public static void saveGame(Game game, String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            Player p1 = game.getPlayer1();
            Player p2 = game.getPlayer2();
            
            // Player 1 Data: Name, Score, Health
            out.println(p1.getName() + "," + p1.getScore() + "," + p1.getHealth());
            // Player 2 Data: Name, Score, Health
            out.println(p2.getName() + "," + p2.getScore() + "," + p2.getHealth());
            // Current Player Name
            out.println(game.getCurrentPlayer().getName());

            // Save board1 Grid and Revealed state
            for (String row : game.getBoard1().serialiseGrid()) out.println(row);
            for (String row : game.getBoard1().serialiseRevealed()) out.println(row);

            // Save board2 Grid and Revealed state
            for (String row : game.getBoard2().serialiseGrid()) out.println(row);
            for (String row : game.getBoard2().serialiseRevealed()) out.println(row);

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Loads a Game state from a file, restoring Player, Current Player, and Board states.
     * @return A restored Game object, or null if loading fails.
     */
    public static Game loadGame(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line1 = br.readLine(); // Player 1
            String line2 = br.readLine(); // Player 2
            String currentName = br.readLine(); // Current Player Name
            
            if (line1 == null || line2 == null || currentName == null) {
                System.out.println("Error loading game: File is incomplete or empty.");
                return null;
            }

            // --- Load Player Data ---
            String[] p1Data = line1.split(",");
            String[] p2Data = line2.split(",");
            
            // Restore Player 1
            Player p1 = new Player(p1Data[0]);
            // IMPORTANT CHANGE: Use setScore to explicitly set the saved score, not add to 0.
            p1.setScore(Integer.parseInt(p1Data[1])); 
            p1.setHealth(Integer.parseInt(p1Data[2]));
            
            // Restore Player 2
            Player p2 = new Player(p2Data[0]);
            // IMPORTANT CHANGE: Use setScore to explicitly set the saved score, not add to 0.
            p2.setScore(Integer.parseInt(p2Data[1])); 
            p2.setHealth(Integer.parseInt(p2Data[2]));

            // --- Load Board 1 Data ---
            String[] grid1 = new String[10];
            for (int i = 0; i < 10; i++) grid1[i] = br.readLine();
            String[] rev1 = new String[10];
            for (int i = 0; i < 10; i++) rev1[i] = br.readLine();
            
            // --- Load Board 2 Data ---
            String[] grid2 = new String[10];
            for (int i = 0; i < 10; i++) grid2[i] = br.readLine();
            String[] rev2 = new String[10];
            for (int i = 0; i < 10; i++) rev2[i] = br.readLine();
            
            // Sanity check for file completion (optional but good practice)
            if (grid1[9] == null || rev2[9] == null) {
                System.out.println("Error loading game: Board data is incomplete.");
                return null;
            }

            // --- Deserialize Boards ---
            Board b1 = new Board(10,10);
            b1.deserialiseGrid(grid1);
            b1.deserialiseRevealed(rev1);
            
            Board b2 = new Board(10,10);
            b2.deserialiseGrid(grid2);
            b2.deserialiseRevealed(rev2);

            // Create and return the restored Game object
            return new Game(p1, p2, currentName, b1, b2);
            
        } catch (FileNotFoundException e) {
            System.out.println("No saved game found: " + filename);
            return null;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading game: Invalid file format or data corruption: " + e.getMessage());
            return null;
        }
    }
}