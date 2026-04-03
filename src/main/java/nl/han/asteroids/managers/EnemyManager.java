package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;
import javafx.scene.paint.Color;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.enemy.Asteroid;
import nl.han.asteroids.entities.enemy.Ufo;
import nl.han.asteroids.factories.EnemyFactory;

import java.util.Random;

public class EnemyManager extends EntitySpawner {

    private final PauseStateProvider pauseStateProvider;
    private final ProjectileManager projectileManager;
    private final ParticleManager particleManager;
    private final GameScoreManager gameScoreManager;
    private final EnemyFactory enemyFactory;
    private final Random random = new Random();

    public EnemyManager(PauseStateProvider pauseStateProvider, GameScoreManager gameScoreManager, ProjectileManager projectileManager, ParticleManager particleManager) {
        super(GameConstants.INITIAL_SPAWN_INTERVAL);
        this.pauseStateProvider = pauseStateProvider;
        this.gameScoreManager = gameScoreManager;
        this.projectileManager = projectileManager;
        this.particleManager = particleManager;
        this.enemyFactory = new EnemyFactory(this, pauseStateProvider, projectileManager);
    }

    @Override
    protected void spawnEntities() {
        if (pauseStateProvider.isPaused()) return;
        
        // Difficulty Scaling: Verlaag interval op basis van score
        long newInterval = Math.max(GameConstants.MIN_SPAWN_INTERVAL, GameConstants.INITIAL_SPAWN_INTERVAL - (gameScoreManager.getScore() / GameConstants.SPAWN_INTERVAL_SCORE_DIVISOR));
        setIntervalInMs(newInterval);

        spawn(enemyFactory.createRandomAsteroidAtEdge());
        
        if (random.nextDouble() < GameConstants.UFO_SPAWN_CHANCE) {
            spawn(enemyFactory.createRandomUfo());
        }
        
        gameScoreManager.checkComboTimeout();
    }
    
    public void spawnAsteroid(Coordinate2D location, int size) {
        spawn(enemyFactory.createAsteroid(location, size));
    }

    public void onAsteroidDestroyed(Asteroid asteroid) {
        gameScoreManager.addScore((4 - asteroid.getSize()) * GameConstants.ASTEROID_BASE_SCORE);
        particleManager.spawnExplosion(asteroid.getAnchorLocation(), Color.LIGHTGRAY, GameConstants.PARTICLES_PER_ASTEROID_EXPLOSION);
    }

    public void onUfoDestroyed(Ufo ufo) {
        gameScoreManager.addScore(GameConstants.UFO_SCORE);
        particleManager.spawnExplosion(ufo.getAnchorLocation(), Color.ORANGE, GameConstants.PARTICLES_PER_UFO_EXPLOSION);
    }
}
