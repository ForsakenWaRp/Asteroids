package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.interfaces.EntityAdder;

/**
 * Hey Nani, dit is de ProjectileManager!
 * Deze manager doet één ding (**Single Responsibility Principle**): het spawnen van projectielen (zoals lasers).
 * Hij gebruikt de EntityAdder interface via **Dependency Injection** om de lasers echt in het spel te plaatsen.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class ProjectileManager {
    private final EntityAdder adder;

    /**
     * Constructor die de EntityAdder injecteert.
     * 
     * @param adder De interface waarmee we projectielen aan de game kunnen toevoegen.
     */
    public ProjectileManager(EntityAdder adder) {
        this.adder = adder;
    }

    /**
     * Spawnt een nieuw projectiel (Laser).
     * 
     * @param location De startlocatie van het projectiel.
     * @param direction De richting waarin het projectiel moet vliegen.
     * @param isEnemyLaser Bepaalt of dit een vijandelijke laser is (true) of van de speler (false).
     */
    public void spawnProjectile(Coordinate2D location, double direction, boolean isEnemyLaser) {
        adder.addNewEntity(new Laser(location, direction, isEnemyLaser));
    }
}
