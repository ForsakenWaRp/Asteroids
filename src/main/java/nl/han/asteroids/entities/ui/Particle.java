package nl.han.asteroids.entities.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import javafx.scene.paint.Color;

import java.util.Random;

public class Particle extends DynamicCircleEntity implements UpdateExposer {

    private double opacity = 1.0;
    private final double fadeSpeed;

    public Particle(Coordinate2D initialLocation, Color color) {
        super(initialLocation);
        var random = new Random();
        
        setRadius(1 + random.nextDouble() * 2);
        setFill(color);
        
        double direction = random.nextDouble() * 360;
        double speed = 1.0 + random.nextDouble() * 3.0;
        setMotion(speed, direction);
        
        this.fadeSpeed = 0.01 + random.nextDouble() * 0.02;
    }

    @Override
    public void explicitUpdate(long timestamp) {
        opacity -= fadeSpeed;
        if (opacity <= 0) {
            remove();
        } else {
            setOpacity(opacity);
        }
    }
}
