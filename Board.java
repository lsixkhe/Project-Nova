package defult;

import java.util.Random;

public class Board {
    private final char[][] grid;
    private final boolean[][] revealed;

    public Board(int rows, int cols) {
        grid = new char[rows][cols];
        revealed = new boolean[rows][cols];
    }

    public void placeCreatures() {
        Random rand = new Random();
        placeRandom('c', 3, rand); // 3 creatures
        placeRandom('b', 2, rand); // 2 booby traps
        placeRandom('p', 2, rand); // 2 power-ups
    }

    private void placeRandom(char symbol, int count, Random rand) {
        int placed = 0;
        while (placed < count) {
            int r = rand.nextInt(grid.length);
            int c = rand.nextInt(grid[0].length);
            if (grid[r][c] == '\u0000') {
                grid[r][c] = symbol;
                placed++;
            }
        }
    }

    // Display modified to show 'X' for empty, revealed cells
    public void display() {
        System.out.print("    ");
        for (int c = 1; c <= grid[0].length; c++) {
            System.out.printf("%2d ", c);
        }
        System.out.println();

        for (int r = 0; r < grid.length; r++) {
            System.out.printf("%2d | ", r + 1);
            for (int c = 0; c < grid[0].length; c++) {
                if (revealed[r][c]) {
                    char cell = grid[r][c];
                    // Show 'X' for empty, revealed cell, otherwise show the content
                    System.out.print((cell == '\u0000' ? 'X' : cell) + "  "); 
                } else {
                    System.out.print(".  ");
                }
            }
            System.out.println();
        }
    }

    // Guess logic
    public boolean guess(int r, int c, Player player) {
        int rr = r - 1;
        int cc = c - 1;
        if (rr < 0 || rr >= grid.length || cc < 0 || cc >= grid[0].length) {
            System.out.println("Out of bounds. Please enter values from 1 to 10.");
            return false;
        }

        if (!revealed[rr][cc]) {
            revealed[rr][cc] = true;
            char cell = grid[rr][cc];

            switch (cell) {
                case 'c':
                    player.addScore(5);
                    System.out.println("Creature found! +5 points");
                    return true;
                case 'b':
                    player.addScore(-2);
                    player.loseHealth(10);
                    System.out.println("Booby trap! -2 points, -10 health");
                    return true;
                case 'p':
                    player.addScore(2);
                    player.gainHealth(10);
                    System.out.println("Power-up! +2 points, +10 health");
                    return true;
                default:
                    // Empty ocean cell found. Returns true to count as a turn.
                    System.out.println("Empty ocean.");
                    return true;
            }
        } else {
            System.out.println("You already checked that coordinate.");
            return false;
        }
    }

    public boolean allCreaturesFound() {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 'c' && !revealed[r][c]) return false;
            }
        }
        return true;
    }
    
    // Hint Method: Reveals one creature and applies score to the player
    public void revealOneCreatureHint(Player player) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                // Check for unrevealed creature ('c')
                if (grid[r][c] == 'c' && !revealed[r][c]) { 
                    revealed[r][c] = true; // Reveal it
                    player.addScore(5); // Apply the creature score
                    System.out.println("HINT ACCEPTED: Creature found at (" + (r + 1) + "," + (c + 1) + ")! +5 points applied to " + player.getName() + ".");
                    return; // Reveal only one and exit
                }
            }
        }
        System.out.println("HINT SKIPPED: All creatures have already been found on your board.");
    }


    // --- Save/Load helpers (unchanged) ---
    public String[] serialiseGrid() {
        String[] rows = new String[grid.length];
        for (int r = 0; r < grid.length; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < grid[0].length; c++) {
                sb.append(grid[r][c] == '\u0000' ? '.' : grid[r][c]);
            }
            rows[r] = sb.toString();
        }
        return rows;
    }

    public String[] serialiseRevealed() {
        String[] rows = new String[revealed.length];
        for (int r = 0; r < revealed.length; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < revealed[0].length; c++) {
                sb.append(revealed[r][c] ? '1' : '0');
            }
            rows[r] = sb.toString();
        }
        return rows;
    }

    public void deserialiseGrid(String[] rows) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                char ch = rows[r].charAt(c);
                grid[r][c] = (ch == '.' ? '\u0000' : ch);
            }
        }
    }

    public void deserialiseRevealed(String[] rows) {
        for (int r = 0; r < revealed.length; r++) {
            for (int c = 0; c < revealed[0].length; c++) {
                revealed[r][c] = rows[r].charAt(c) == '1';
            }
        }
    }
}