package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.interfaces.EntityAdder;

public class ProjectileManager {
    private final EntityAdder adder;

    public ProjectileManager(EntityAdder adder) {
        this.adder = adder;
    }

    public void spawnProjectile(Coordinate2D location, double direction, boolean isEnemyLaser) {
        adder.addNewEntity(new Laser(location, direction, isEnemyLaser));
    }
}
