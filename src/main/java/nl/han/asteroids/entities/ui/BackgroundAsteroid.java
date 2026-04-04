package nl.han.asteroids.entities.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import nl.han.asteroids.config.GameConstants;

import java.util.Random;

/**
 * Hey Nani! Dit is een asteroïde puur voor visuele flair in het menu.
 * Heeft GEEN collision en is alleen maar decoratie.
 * 
 * OOP Principes:
 * - Overerving: Breidt DynamicSpriteEntity uit voor weergave en beweging.
 * - Interfaces: Implementeert SceneBorderCrossingWatcher voor screen-wrapping.
 * 
 * Han Yaeger Engine:
 * - DynamicSpriteEntity: Zorgt dat het een bewegende afbeelding is.
 * - SceneBorderCrossingWatcher: Zorgt dat asteroïden terugkomen in het scherm als ze eruit vliegen.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class BackgroundAsteroid extends DynamicSpriteEntity implements SceneBorderCrossingWatcher {

    /**
     * Constructor maakt de achtergrond-asteroïde aan op een willekeurige locatie en geeft hem een trage snelheid.
     * @param initialLocation Startpositie (x, y).
     */
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

    /**
     * @Override van SceneBorderCrossingWatcher.
     * Als deze achtergrond asteroïde het scherm uitgaat, komt hij aan de overkant weer terug.
     * @param border De rand van de scene.
     */
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