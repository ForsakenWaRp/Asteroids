package nl.han.asteroids.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Toont de score en pulseert kort bij verandering.
 */
public class ScoreText extends TextEntity implements UpdateExposer {

    private double currentScale = 1.0;
    private static final double TARGET_SCALE = 1.3;
    private static final double PULSE_SPEED = 0.05;

    public ScoreText(Coordinate2D initialLocation) {
        super(initialLocation, "Score: 0");
        setFill(Color.WHITE);
        setFont(Font.font("Roboto", FontWeight.BOLD, 24));
    }

    public void setScore(int score, int multiplier) {
        String text = "Score: " + score;
        if (multiplier > 1) {
            text += " (x" + multiplier + ")";
        }
        setText(text);
        currentScale = TARGET_SCALE;
    }

    @Override
    public void explicitUpdate(long timestamp) {
        if (currentScale > 1.0) {
            currentScale -= PULSE_SPEED;
            if (currentScale < 1.0) currentScale = 1.0;
            
            // Pas font grootte aan voor pulse effect
            setFont(Font.font("Roboto", FontWeight.BOLD, 24 * currentScale));
        }
    }
}
