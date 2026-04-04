package nl.han.asteroids.managers;

import nl.han.asteroids.config.GameConstants;

/**
 * Hey Nani, welkom bij de GameScoreManager!
 * Deze klasse volgt het **Single Responsibility Principle**: hij doet niets anders dan het bijhouden
 * van de score en de combo-multiplier tijdens een actieve game. Via de 'Runnable onScoreChange' passen 
 * we een simpele vorm van het **Observer/Callback patroon** toe om de interface op de hoogte te stellen van scoreveranderingen.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class GameScoreManager {
    private int score = 0;
    private int multiplier = 1;
    private long lastHitTime = 0;
    private final Runnable onScoreChange;

    /**
     * Constructor die een callback accepteert.
     * 
     * @param onScoreChange De actie die uitgevoerd moet worden zodra de score verandert.
     */
    public GameScoreManager(Runnable onScoreChange) {
        this.onScoreChange = onScoreChange;
    }

    /**
     * Voegt punten toe aan de huidige score en werkt de combo bij.
     * 
     * @param points Het aantal basispunten om toe te voegen.
     */
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

    /**
     * Controleert of de combo is verlopen (als je te lang niets hebt geraakt).
     */
    public void checkComboTimeout() {
        if (System.currentTimeMillis() - lastHitTime > GameConstants.COMBO_TIMEOUT && multiplier > 1) {
            multiplier = 1;
            notifyScoreChange();
        }
    }

    private void notifyScoreChange() {
        if (onScoreChange != null) onScoreChange.run();
    }

    /**
     * Geeft de huidige score terug.
     * 
     * @return De score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Geeft de huidige combo multiplier terug.
     * 
     * @return De multiplier.
     */
    public int getMultiplier() {
        return multiplier;
    }
}
