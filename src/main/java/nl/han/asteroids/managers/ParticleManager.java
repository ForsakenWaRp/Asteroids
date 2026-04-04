package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import javafx.scene.paint.Color;
import nl.han.asteroids.entities.ui.Particle;
import nl.han.asteroids.interfaces.EntityAdder;

/**
 * Hey Nani! De ParticleManager zorgt voor explosies en deeltjes.
 * Ook hier zien we het **Single Responsibility Principle**. De manager coördineert het maken van deeltjes
 * en gebruikt de EntityAdder interface (via **Dependency Injection**) om ze aan de wereld toe te voegen.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class ParticleManager {
    private final EntityAdder adder;

    /**
     * Constructor met Dependency Injection voor de EntityAdder.
     * 
     * @param adder Een interface om deeltjes aan het spel toe te voegen.
     */
    public ParticleManager(EntityAdder adder) {
        this.adder = adder;
    }

    /**
     * Spawnt een explosie van deeltjes op een specifieke locatie.
     * 
     * @param location De locatie van de explosie.
     * @param color De kleur van de deeltjes.
     * @param amount Het aantal deeltjes dat gespawnd moet worden.
     */
    public void spawnExplosion(Coordinate2D location, Color color, int amount) {
        for (int i = 0; i < amount; i++) {
            adder.addNewEntity(new Particle(location, color));
        }
    }
}
