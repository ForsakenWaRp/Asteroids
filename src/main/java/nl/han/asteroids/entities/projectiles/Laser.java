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
 * Representeert een laserstraal afgevuurd door de speler of een UFO.
 */
public class Laser extends DynamicSpriteEntity implements SceneBorderCrossingWatcher, Collider, Hittable {

    private final boolean isEnemyLaser;

    public Laser(Coordinate2D initialLocation, double direction, boolean isEnemyLaser) {
        super(isEnemyLaser ? "sprites/laser_2.png" : "sprites/laser_1.png", initialLocation, new Size(16, 16));
        this.isEnemyLaser = isEnemyLaser;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setMotion(GameConstants.LASER_SPEED, direction);
    }

    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof BaseEnemy && !isEnemyLaser) {
            remove();
        } else if (collider instanceof PlayerSpaceship && isEnemyLaser) {
            remove();
        }
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        remove();
    }

    public boolean isEnemyLaser() {
        return isEnemyLaser;
    }
}
