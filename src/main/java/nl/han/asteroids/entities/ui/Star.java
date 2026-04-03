package nl.han.asteroids.entities.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import javafx.scene.paint.Color;
import nl.han.asteroids.config.GameConstants;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;

import java.util.Random;

/**
 * Een eenvoudige ster voor de achtergrond.
 * Beweegt langzaam om een gevoel van diepte (parallax) te geven.
 */
public class Star extends DynamicCircleEntity implements SceneBorderCrossingWatcher {

    public Star(Coordinate2D initialLocation, int layer) {
        super(initialLocation);
        
        var random = new Random();
        double size = (0.5 + random.nextDouble() * 1.5) * (layer == 1 ? 0.5 : layer == 2 ? 1.0 : 1.5);
        setRadius(size);
        
        double opacity = (0.3 + random.nextDouble() * 0.7) * (layer == 1 ? 0.4 : layer == 2 ? 0.7 : 1.0);
        setFill(Color.color(1, 1, 1, opacity));
        
        // Snelheid hangt af van de laag (parallax)
        double speed = (0.05 + random.nextDouble() * 0.15) * (layer == 1 ? 0.5 : layer == 2 ? 1.0 : 2.0);
        setMotion(speed, 180 + (random.nextDouble() * 20 - 10));
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        var random = new Random();
        if (border == SceneBorder.LEFT) {
            setAnchorLocation(new Coordinate2D(GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT));
        } else if (border == SceneBorder.TOP) {
            setAnchorLocation(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, GameConstants.HEIGHT));
        } else if (border == SceneBorder.RIGHT) {
            setAnchorLocation(new Coordinate2D(0, random.nextDouble() * GameConstants.HEIGHT));
        } else if (border == SceneBorder.BOTTOM) {
            setAnchorLocation(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, 0));
        }
    }
}
