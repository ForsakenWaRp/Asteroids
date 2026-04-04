package nl.han.asteroids.ui;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseEnterListener;
import com.github.hanyaeger.api.userinput.MouseExitListener;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.github.hanyaeger.api.UpdateExposer;

/**
 * Kijk Nani, dit is een herbruikbare knop (AsteroidsButton)! 
 * Hij erft van TextEntity, want een knop is eigenlijk gewoon een stukje tekst waarop je kunt klikken.
 * Het gebruikt interfaces voor muis-interacties (MouseEnterListener, MouseExitListener, MouseButtonPressedListener)
 * en UpdateExposer om een pulserend effect te maken als de knop geselecteerd is.
 * Dit is een mooi voorbeeld van Encapsulatie, want de knop beheert zelf zijn kleuren en selectiestatus.
 * 
 * Han Yaeger documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class AsteroidsButton extends TextEntity implements MouseEnterListener, MouseExitListener, MouseButtonPressedListener, UpdateExposer {

    private final Runnable onClick;
    private String originalText;
    private boolean isSelected = false;
    private double pulseValue = 0;

    /**
     * Constructor voor een knop.
     * @param initialLocation De X en Y coördinaten.
     * @param text De tekst op de knop.
     * @param onClick Een stukje code (Runnable) dat wordt uitgevoerd als je klikt.
     */
    public AsteroidsButton(Coordinate2D initialLocation, String text, Runnable onClick) {
        super(initialLocation, text);
        this.onClick = onClick;
        this.originalText = text;

        setFill(Color.WHITE);
        setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }

    /**
     * Past de tekst op de knop aan.
     * @param newText De nieuwe tekst.
     */
    public void updateText(String newText) {
        this.originalText = newText;
        setText(newText);
    }

    /**
     * Markeer de knop als geselecteerd, zodat hij kan gaan pulseren.
     * @param selected True als hij geselecteerd is, anders false.
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        setFill(selected ? Color.YELLOW : Color.WHITE);
        setText(originalText);
    }

    /**
     * @Override: Wordt door de game loop aangeroepen om de knop te updaten (UpdateExposer).
     * @param timestamp De huidige tijd in de game loop.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        if (isSelected) {
            pulseValue += 0.1;
            double brightness = 0.7 + Math.sin(pulseValue) * 0.3;
            setFill(Color.color(1.0, 1.0, 0, brightness));
        }
    }

    /**
     * @Override: Wat er moet gebeuren als de muisklik plaatsvindt.
     */
    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY) {
            onClick.run();
        }
    }

    /**
     * @Override: Verandert de muisaanwijzer in een handje als je eroverheen zweeft.
     */
    @Override
    public void onMouseEntered() {
        setCursor(Cursor.HAND);
    }

    /**
     * @Override: Verandert de muisaanwijzer weer terug.
     */
    @Override
    public void onMouseExited() {
        setCursor(Cursor.DEFAULT);
    }
}
