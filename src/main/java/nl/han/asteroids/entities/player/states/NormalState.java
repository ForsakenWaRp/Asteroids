package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.entities.enemy.BaseEnemy;
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
            if (col instanceof Laser laser && laser.isEnemyLaser()) {
                laser.remove();
                player.destroy();
                return;
            }
            if (col instanceof BaseEnemy) {
                player.destroy();
                return;
            }
        }
    }
}
