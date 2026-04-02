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

/**
 * Wat de Class doet: Een generieke knopcomponent voor UI-interacties.
 * Hoe het werkt in de Gamelogica: Vergelijkbaar met AsteroidsButton, biedt interactieve tekst die acties kan triggeren.
 * OOP Principes: Inheritance (TextEntity), Polymorphism (implementeert Mouse-interfaces).
 */
public class Button extends TextEntity implements MouseEnterListener, MouseExitListener, MouseButtonPressedListener {

    private final Runnable onClick;

    public Button(Coordinate2D initialLocation, String text, Runnable onClick) {
        super(initialLocation, text);
        this.onClick = onClick;
        setFill(Color.WHITE);
        setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY) {
            onClick.run();
        }
    }

    @Override
    public void onMouseEntered() {
        setFill(Color.LIGHTGRAY);
        setCursor(Cursor.HAND);
    }

    @Override
    public void onMouseExited() {
        setFill(Color.WHITE);
        setCursor(Cursor.DEFAULT);
    }
}