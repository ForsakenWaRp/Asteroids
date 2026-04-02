package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;
import nl.han.asteroids.entities.ui.Particle;
import nl.han.asteroids.interfaces.EntityAdder;

public class ParticleManager {
    private final EntityAdder adder;

    public ParticleManager(EntityAdder adder) {
        this.adder = adder;
    }

    public void spawnExplosion(Coordinate2D location, Color color, int amount) {
        for (int i = 0; i < amount; i++) {
            adder.addNewEntity(new Particle(location, color));
        }
    }
}
