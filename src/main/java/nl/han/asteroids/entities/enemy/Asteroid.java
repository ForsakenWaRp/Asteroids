package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.Size;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.managers.EnemyManager;
import nl.han.asteroids.managers.SoundManager;

import java.util.List;
import java.util.Random;

/**
 * Hey Nani! Deze klasse representeert een asteroïde in het spel.
 * 
 * OOP Principes:
 * - Overerving (Inheritance): Deze klasse extends BaseEnemy.
 * - Interfaces: Deze klasse implementeert Collided en Collider voor botsingen, en SceneBorderCrossingWatcher om schermranden te detecteren.
 * 
 * Han Yaeger Engine:
 * - DynamicSpriteEntity (via BaseEnemy): Zorgt ervoor dat dit een bewegend plaatje is.
 * - SceneBorderCrossingWatcher: Laat weten wanneer we de rand van het scherm bereiken.
 * - Collided / Collider: Han Yaeger's manier om botsingen af te handelen.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class Asteroid extends BaseEnemy implements SceneBorderCrossingWatcher, Collided, Collider {
    private final int size;

    /**
     * De constructor maakt de asteroïde aan en geeft hem een richting en snelheid.
     * @param initialLocation Startpositie (x, y)
     * @param direction De hoek/richting waarin de asteroïde vliegt
     * @param speed De snelheid
     * @param size De grootte (3 is groot, 2 is middel, 1 is klein)
     * @param enemyManager Beheert alle vijanden
     * @param pauseStateProvider Vertelt of het spel gepauzeerd is
     */
    public Asteroid(Coordinate2D initialLocation, double direction, double speed, int size, EnemyManager enemyManager, PauseStateProvider pauseStateProvider) {
        super(getRandomSprite(size), initialLocation, getActualSize(size), enemyManager, pauseStateProvider);
        this.size = size;
        setMotion(speed, direction);
    }

    private static String getRandomSprite(int size) {
        int v = new Random().nextInt(3) + 1;
        String s = (size == 3) ? "large" : (size == 2) ? "medium" : "small";
        return "sprites/asteroid_" + s + "_" + v + ".png";
    }

    private static Size getActualSize(int size) {
        return (size == 3) ? new Size(160, 160) : (size == 2) ? new Size(96, 96) : new Size(64, 64);
    }

    /**
     * @Override betekent dat we een methode uit de BaseEnemy klasse overschrijven.
     * Voor de asteroïde hoeven we hier niets speciaals te updaten.
     */
    @Override
    protected void updateEnemy(long timestamp) {}

    /**
     * @Override van SceneBorderCrossingWatcher.
     * Zorgt voor het 'screen wrap' effect (als je links het scherm uitgaat, kom je rechts terug).
     * @param border De rand van het scherm die we kruisen.
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

    /**
     * @Override van Collided.
     * Wordt door Han Yaeger aangeroepen als we met andere objecten (Colliders) botsen.
     * We gebruiken hier de Stelling van Pythagoras om te kijken of de cirkels elkaar écht raken.
     * @param collidingObjects De lijst van objecten die we raken.
     */
    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (pauseStateProvider.isPaused()) return;
        
        // Bereken ons eigen middelpunt (aangezien de asteroid een vierkante sprite is, is het midden + de helft)
        double myCenterX = getAnchorLocation().getX() + (getWidth() / 2);
        double myCenterY = getAnchorLocation().getY() + (getHeight() / 2);
        double myRadius = getWidth() / 2;

        for (Collider col : collidingObjects) {
            // Bereken middelpunt van het object dat ons raakt
            double otherCenterX = col.getBoundingBox().getMinX() + (col.getBoundingBox().getWidth() / 2);
            double otherCenterY = col.getBoundingBox().getMinY() + (col.getBoundingBox().getHeight() / 2);
            
            // Gebruik een geschatte radius voor het andere object (helft van zijn breedte)
            double otherRadius = col.getBoundingBox().getWidth() / 2;

            // Stelling van Pythagoras: a^2 + b^2 = c^2
            double dx = myCenterX - otherCenterX;
            double dy = myCenterY - otherCenterY;
            double distanceSquared = (dx * dx) + (dy * dy);
            
            // Controleer of de afstand tussen de middelpunten kleiner is dan de twee radiussen bij elkaar
            double radiusSum = myRadius + otherRadius;
            if (distanceSquared < (radiusSum * radiusSum)) {
                // Het is een échte ronde hit!
                this.onHitBy(col);
                if (col instanceof nl.han.asteroids.interfaces.Hittable hittable) {
                    hittable.onHitBy(this);
                }
            }
        }
    }

    /**
     * @Override van een custom interface of BaseEnemy (indien van toepassing).
     * Hier passen we 'Tell Don't Ask' toe: we vernietigen onszelf als we door een speler-laser geraakt worden.
     * @param collider Het object dat ons raakt.
     */
    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof Laser laser && !laser.isEnemyLaser()) {
            destroy();
        }
    }

    private void destroy() {
        SoundManager.play(SoundManager.SoundType.ASTEROID_EXPLOSION);
        enemyManager.onAsteroidDestroyed(this);
        remove();
        if (size > 1) {
            enemyManager.spawnAsteroid(getAnchorLocation(), size - 1);
            enemyManager.spawnAsteroid(getAnchorLocation(), size - 1);
        }
    }

    /**
     * Geeft de grootte van de asteroïde terug.
     * @return de grootte.
     */
    public int getSize() { return size; }
}