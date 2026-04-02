package nl.han.asteroids.models;

/**
 * Model klasse voor een speler score.
 */
public class PlayerScore {
    private String name;
    private int score;

    public PlayerScore() {} // Nodig voor Jackson

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
