package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.config.GameConstants;
import java.util.List;

/**
 * De toestand waarin de speler tijdelijk onkwetsbaar is na respawn.
 */
public class InvulnerableState extends BasePlayerState {
    private final long spawnTime;

    public InvulnerableState(PlayerSpaceship player) {
        super(player);
        this.spawnTime = System.currentTimeMillis();
    }

    @Override
    public void enter() {
        player.setOpacity(0.5);
    }

    @Override
    public void update(long timestamp) {
        super.update(timestamp);
        if (System.currentTimeMillis() - spawnTime > GameConstants.INVULNERABILITY_DURATION) {
            player.setState(new NormalState(player));
        }
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        // Negeer botsingen tijdens onsterfelijkheid
    }
}
