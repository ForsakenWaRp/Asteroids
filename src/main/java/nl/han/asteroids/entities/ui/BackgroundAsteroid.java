package nl.han.asteroids.entities.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import nl.han.asteroids.config.GameConstants;

import java.util.Random;

/**
 * Een asteroïde puur voor visuele flair in het menu.
 * Heeft geen collision en beweegt langzaam op de achtergrond.
 */
public class BackgroundAsteroid extends DynamicSpriteEntity implements SceneBorderCrossingWatcher {

    public BackgroundAsteroid(Coordinate2D initialLocation) {
        super(getRandomSprite(), initialLocation, getActualSize());
        
        var random = new Random();
        setOpacity(0.3 + random.nextDouble() * 0.3);
        setMotion(0.2 + random.nextDouble() * 0.5, random.nextDouble() * 360);
        setRotate(random.nextDouble() * 360);
    }

    private static String getRandomSprite() {
        int v = new Random().nextInt(3) + 1;
        return "sprites/asteroid_large_" + v + ".png";
    }

    private static Size getActualSize() {
        return new Size(160, 160);
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        double x = getAnchorLocation().getX();
        double y = getAnchorLocation().getY();
        if (border == SceneBorder.LEFT) x = GameConstants.WIDTH;
        else if (border == SceneBorder.RIGHT) x = 0;
        else if (border == SceneBorder.TOP) y = GameConstants.HEIGHT;
        else if (border == SceneBorder.BOTTOM) y = 0;
        setAnchorLocation(new Coordinate2D(x, y));
    }
}
