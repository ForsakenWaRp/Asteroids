package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import java.util.List;

/**
 * De toestand waarin de speler tijdelijk onkwetsbaar is na respawn.
 */
public class InvulnerableState extends BasePlayerState {

    public InvulnerableState(PlayerSpaceship player) {
        super(player);
    }

    @Override
    public void enter() {
        player.setOpacity(0.5);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        // Negeer botsingen tijdens onsterfelijkheid
    }

    @Override
    public void onHitBy(Collider collider) {
        // Negeer hits tijdens onsterfelijkheid
    }
}
