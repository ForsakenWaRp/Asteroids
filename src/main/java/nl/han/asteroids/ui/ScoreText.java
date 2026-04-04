package nl.han.asteroids.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Nani, dit is de ScoreText! Hierin wordt de score van de speler getoond.
 * Het erft van TextEntity (een speciaal tekstelement in Han Yaeger) en implementeert UpdateExposer.
 * Dit zorgt ervoor dat de tekst een leuk pulserend effect kan hebben als de score verandert.
 * De variabelen voor het pulseren (zoals currentScale) zijn netjes verborgen (Encapsulatie).
 * 
 * Han Yaeger documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class ScoreText extends TextEntity implements UpdateExposer {

    private double currentScale = 1.0;
    private static final double TARGET_SCALE = 1.3;
    private static final double PULSE_SPEED = 0.05;

    /**
     * De constructor van ScoreText.
     * @param initialLocation Waar op het scherm de tekst moet beginnen.
     */
    public ScoreText(Coordinate2D initialLocation) {
        super(initialLocation, "Score: 0");
        setFill(Color.WHITE);
        setFont(Font.font("Roboto", FontWeight.BOLD, 24));
    }

    /**
     * Verandert de score en start de pulserende animatie.
     * @param score De nieuwe score.
     * @param multiplier Een vermenigvuldiger, bijvoorbeeld voor extra punten.
     */
    public void setScore(int score, int multiplier) {
        String text = "Score: " + score;
        if (multiplier > 1) {
            text += " (x" + multiplier + ")";
        }
        setText(text);
        currentScale = TARGET_SCALE;
    }

    /**
     * @Override: Dit wordt elke 'tick' van de game loop aangeroepen dankzij UpdateExposer.
     * Hier maken we het lettertype langzaam weer kleiner na een score-update.
     */
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
