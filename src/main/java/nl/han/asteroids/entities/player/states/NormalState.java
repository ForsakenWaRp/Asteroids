package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.entities.enemy.BaseEnemy;
import nl.han.asteroids.interfaces.Hittable;
import java.util.List;

/**
 * De toestand waarin de speler kwetsbaar is.
 */
public class NormalState extends BasePlayerState {
    public NormalState(PlayerSpaceship player) {
        super(player);
    }

    @Override
    public void enter() {
        player.setOpacity(1.0);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider col : collidingObjects) {
            player.onHitBy(col);
            if (col instanceof Hittable hittable) {
                hittable.onHitBy(player);
            }
        }
    }

    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof BaseEnemy) {
            player.destroy();
        } else if (collider instanceof Laser laser && laser.isEnemyLaser()) {
            player.destroy();
        }
    }
}
