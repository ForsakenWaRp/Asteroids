package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.Set;

public interface PlayerState {
    void handleInput(Set<KeyCode> pressedKeys);
    void update(long timestamp);
    void onCollision(List<Collider> collidingObjects);
    void onHitBy(Collider collider);
    void enter();
}
