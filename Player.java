package defult;

public class Player {
    private String name;
    private int score;
    private int health;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.health = 100;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public void addScore(int points) { score += points; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public void loseHealth(int amount) { health = Math.max(0, health - amount); }
    public void gainHealth(int amount) { health = Math.min(100, health + amount); }
    public void setScore(int score) { this.score = score; }
}
