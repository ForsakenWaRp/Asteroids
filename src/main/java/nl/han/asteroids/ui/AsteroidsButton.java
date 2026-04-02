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

public class AsteroidsButton extends TextEntity implements MouseEnterListener, MouseExitListener, MouseButtonPressedListener {

    private final Runnable onClick;
    private String originalText;
    private boolean isSelected = false;

    public AsteroidsButton(Coordinate2D initialLocation, String text, Runnable onClick) {
        super(initialLocation, text);
        this.onClick = onClick;
        this.originalText = text;

        setFill(Color.WHITE);
        setFont(Font.font("Roboto", FontWeight.BOLD, 30));
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }

    public void updateText(String newText) {
        this.originalText = newText;
        setText(newText);
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        setFill(selected ? Color.YELLOW : Color.WHITE);
        setText(originalText);
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY) {
            onClick.run();
        }
    }

    @Override
    public void onMouseEntered() {
        setCursor(Cursor.HAND);
    }

    @Override
    public void onMouseExited() {
        setCursor(Cursor.DEFAULT);
    }
}
