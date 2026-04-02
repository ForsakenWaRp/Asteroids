package nl.han.asteroids.managers;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;
import javafx.scene.paint.Color;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.enemy.Asteroid;
import nl.han.asteroids.entities.enemy.Ufo;

import java.util.Random;

public class EnemyManager extends EntitySpawner {

    private final AsteroidsGame asteroidsGame;
    private final ProjectileManager projectileManager;
    private final ParticleManager particleManager;
    private final Random random = new Random();
    private int score = 0;
    private final Runnable onScoreChange;

    private int multiplier = 1;
    private long lastHitTime = 0;
    private static final long COMBO_TIMEOUT = 2000;
    
    private static final long MIN_SPAWN_INTERVAL = 800;
    private static final long INITIAL_SPAWN_INTERVAL = 3000;

    public EnemyManager(AsteroidsGame asteroidsGame, Runnable onScoreChange, ProjectileManager projectileManager, ParticleManager particleManager) {
        super(INITIAL_SPAWN_INTERVAL);
        this.asteroidsGame = asteroidsGame;
        this.onScoreChange = onScoreChange;
        this.projectileManager = projectileManager;
        this.particleManager = particleManager;
    }

    @Override
    protected void spawnEntities() {
        if (asteroidsGame.isPaused()) return;
        
        // Difficulty Scaling: Verlaag interval op basis van score
        long newInterval = Math.max(MIN_SPAWN_INTERVAL, INITIAL_SPAWN_INTERVAL - (score / 10));
        setIntervalInMs(newInterval);

        spawnAsteroidAtEdge();
        if (random.nextDouble() < 0.20) spawnUfo();
        
        if (System.currentTimeMillis() - lastHitTime > COMBO_TIMEOUT && multiplier > 1) {
            multiplier = 1;
            notifyScoreChange();
        }
    }

    private void spawnAsteroidAtEdge() {
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
        spawn(new Asteroid(new Coordinate2D(x, y), direction, speed, 3, this, asteroidsGame));
    }
    
    private void spawnUfo() {
        double y = random.nextDouble() * GameConstants.HEIGHT;
        spawn(new Ufo(new Coordinate2D(-50, y), projectileManager, this, asteroidsGame));
    }
    
    public void spawnAsteroid(Coordinate2D location, int size) {
        double direction = random.nextDouble() * 360;
        double speed = 2.0 + random.nextDouble() * 3.0;
        spawn(new Asteroid(location, direction, speed, size, this, asteroidsGame));
    }

    public void onAsteroidDestroyed(Asteroid asteroid) {
        updateCombo();
        score += (4 - asteroid.getSize()) * 10 * multiplier;
        notifyScoreChange();
        particleManager.spawnExplosion(asteroid.getAnchorLocation(), Color.LIGHTGRAY, 10);
    }

    public void onUfoDestroyed(Ufo ufo) {
        score += 100 * multiplier;
        notifyScoreChange();
        particleManager.spawnExplosion(ufo.getAnchorLocation(), Color.ORANGE, 20);
    }

    private void updateCombo() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime < COMBO_TIMEOUT) {
            multiplier++;
        } else {
            multiplier = 1;
        }
        lastHitTime = currentTime;
    }
    
    private void notifyScoreChange() {
        if (onScoreChange != null) onScoreChange.run();
    }
    
    public int getScore() {
        return score;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
