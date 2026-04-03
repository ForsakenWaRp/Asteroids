package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import javafx.scene.input.KeyCode;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import java.util.List;
import java.util.Set;

/**
 * Gedeelde basislogica voor alle speler states.
 */
public abstract class BasePlayerState implements PlayerState {
   
    protected final PlayerSpaceship player;

    public BasePlayerState(PlayerSpaceship player) {
        this.player = player;
    }

    @Override
    public void handleInput(Set<KeyCode> pressedKeys) {
        player.setRotatingLeft(pressedKeys.contains(KeyCode.LEFT));
        player.setRotatingRight(pressedKeys.contains(KeyCode.RIGHT));
        player.setAccelerating(pressedKeys.contains(KeyCode.UP));

        if (pressedKeys.contains(KeyCode.SPACE)) {
            player.handleShooting();
        }
    }

    @Override
    public void update(long timestamp) {
        player.applyPhysics();
    }

    @Override
    public abstract void onCollision(List<Collider> collidingObjects);

    @Override
    public abstract void onHitBy(Collider collider);

    @Override
    public void enter() {}
}
