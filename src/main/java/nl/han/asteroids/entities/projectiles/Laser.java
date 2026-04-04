package nl.han.asteroids.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.enemy.BaseEnemy;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.interfaces.Hittable;
import java.util.List;

/**
 * Hey Nani! Deze klasse representeert een laserstraal afgevuurd door de speler of een UFO.
 * 
 * OOP Principes:
 * - Overerving: Breidt DynamicSpriteEntity uit zodat de laser als een bewegend plaatje getekend wordt.
 * - Interfaces: Implementeert Collider en Hittable voor botsingen.
 * - Tell Don't Ask: De laser controleert zélf of hij een speler of vijand raakt, en verwijdert zichzelf indien nodig.
 * 
 * Han Yaeger Engine:
 * - DynamicSpriteEntity: Basis voor een bewegende entity met een afbeelding.
 * - SceneBorderCrossingWatcher: Om te zien of de laser het scherm verlaat.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class Laser extends DynamicSpriteEntity implements SceneBorderCrossingWatcher, Collider, Hittable {

    private final boolean isEnemyLaser;

    /**
     * Constructor om een laser af te vuren.
     * @param initialLocation De beginpositie (x, y) van de laser.
     * @param direction De richting waarin de laser vliegt.
     * @param isEnemyLaser True als dit de laser van de UFO is, false als het van de speler is.
     */
    public Laser(Coordinate2D initialLocation, double direction, boolean isEnemyLaser) {
        super(isEnemyLaser ? "sprites/laser_2.png" : "sprites/laser_1.png", initialLocation, new Size(16, 16));
        this.isEnemyLaser = isEnemyLaser;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setMotion(GameConstants.LASER_SPEED, direction);
    }

    /**
     * @Override van Hittable.
     * Wordt aangeroepen als we met iets botsen.
     * Als een speler-laser een vijand raakt, of een vijand-laser een speler, verdwijnt de laser.
     */
    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof BaseEnemy && !isEnemyLaser) {
            remove();
        } else if (collider instanceof PlayerSpaceship && isEnemyLaser) {
            remove();
        }
    }

    /**
     * @Override van SceneBorderCrossingWatcher.
     * Verwijder de laser uit de game als deze het scherm verlaat.
     */
    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        remove();
    }

    /**
     * Geeft aan of deze laser door een vijand is afgevuurd.
     * @return true als het een vijand-laser is.
     */
    public boolean isEnemyLaser() {
        return isEnemyLaser;
    }
}