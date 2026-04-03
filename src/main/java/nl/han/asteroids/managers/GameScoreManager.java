package nl.han.asteroids.managers;

import nl.han.asteroids.config.GameConstants;

/**
 * Beheert de score en de combo-multiplier tijdens een actieve game.
 */
public class GameScoreManager {
    private int score = 0;
    private int multiplier = 1;
    private long lastHitTime = 0;
    private final Runnable onScoreChange;

    public GameScoreManager(Runnable onScoreChange) {
        this.onScoreChange = onScoreChange;
    }

    public void addScore(int points) {
        updateCombo();
        score += points * multiplier;
        notifyScoreChange();
    }

    private void updateCombo() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime < GameConstants.COMBO_TIMEOUT) {
            multiplier++;
        } else {
            multiplier = 1;
        }
        lastHitTime = currentTime;
    }

    public void checkComboTimeout() {
        if (System.currentTimeMillis() - lastHitTime > GameConstants.COMBO_TIMEOUT && multiplier > 1) {
            multiplier = 1;
            notifyScoreChange();
        }
    }

    private void notifyScoreChange() {
        if (onScoreChange != null) onScoreChange.run();
    }

    public int getScore() {
        return score;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
