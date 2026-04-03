package nl.han.asteroids.factories;

import com.github.hanyaeger.api.Coordinate2D;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.enemy.Asteroid;
import nl.han.asteroids.entities.enemy.Ufo;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.managers.EnemyManager;
import nl.han.asteroids.managers.ProjectileManager;

import java.util.Random;

/**
 * Factory class responsible for creating enemy entities.
 * Encapsulates the instantiation logic and configuration of enemies.
 */
public class EnemyFactory {

    private final EnemyManager enemyManager;
    private final PauseStateProvider pauseStateProvider;
    private final ProjectileManager projectileManager;
    private final Random random = new Random();

    public EnemyFactory(EnemyManager enemyManager, PauseStateProvider pauseStateProvider, ProjectileManager projectileManager) {
        this.enemyManager = enemyManager;
        this.pauseStateProvider = pauseStateProvider;
        this.projectileManager = projectileManager;
    }

    public Asteroid createRandomAsteroidAtEdge() {
        double x = 0, y = 0;
        if (random.nextBoolean()) {
            x = random.nextDouble() * GameConstants.WIDTH;
            y = random.nextBoolean() ? 0 : GameConstants.HEIGHT;
        } else {
            x = random.nextBoolean() ? 0 : GameConstants.WIDTH;
            y = random.nextDouble() * GameConstants.HEIGHT;
        }
        double direction = random.nextDouble() * 360;
        double speed = 1.0 + random.nextDouble() * 2.0;
        return new Asteroid(new Coordinate2D(x, y), direction, speed, GameConstants.ASTEROID_MAX_SIZE, enemyManager, pauseStateProvider);
    }

    public Ufo createRandomUfo() {
        double y = random.nextDouble() * GameConstants.HEIGHT;
        return new Ufo(new Coordinate2D(-50, y), projectileManager, enemyManager, pauseStateProvider);
    }

    public Asteroid createAsteroid(Coordinate2D location, int size) {
        double direction = random.nextDouble() * 360;
        double speed = 2.0 + random.nextDouble() * 3.0;
        return new Asteroid(location, direction, speed, size, enemyManager, pauseStateProvider);
    }
}
