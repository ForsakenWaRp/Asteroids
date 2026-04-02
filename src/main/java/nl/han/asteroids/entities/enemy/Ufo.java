package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.EnemyManager;
import nl.han.asteroids.managers.ProjectileManager;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.managers.SoundManager;

import java.util.List;
import java.util.Random;

/**
 * Vertegenwoordigt de UFO vijand.
 * 
 * OOP Principes:
 * - Overerving: Breidt BaseEnemy uit.
 */
public class Ufo extends BaseEnemy implements SceneBorderCrossingWatcher, Collided, Collider {

    private final ProjectileManager projectileManager;
    private long lastShotTime = 0;
    private static final long SHOT_INTERVAL = GameConstants.SHOOT_INTERVAL_UFO;
    private final Random random = new Random();
    private boolean soundPaused = false;

    public Ufo(Coordinate2D initialLocation, ProjectileManager projectileManager, EnemyManager enemyManager, AsteroidsGame asteroidsGame) {
        super("sprites/ufo.png", initialLocation, new Size(96, 80), enemyManager, asteroidsGame);
        this.projectileManager = projectileManager;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setMotion(4.0, 90);
        SoundManager.play(SoundManager.SoundType.UFO_ENGINE);
    }

    @Override
    protected void updateEnemy(long timestamp) {
        if (soundPaused) {
            SoundManager.play(SoundManager.SoundType.UFO_ENGINE);
            soundPaused = false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime > SHOT_INTERVAL) {
            double direction = random.nextDouble() * 360;
            projectileManager.spawnProjectile(getAnchorLocation(), direction, true);
            SoundManager.play(SoundManager.SoundType.UFO_FIRE);
            lastShotTime = currentTime;
        }
    }

    @Override
    public void explicitUpdate(long timestamp) {
        super.explicitUpdate(timestamp);
        
        if (asteroidsGame.isPaused() && !soundPaused) {
            SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
            soundPaused = true;
        }
    }

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        if (border == SceneBorder.RIGHT) {
            SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
            remove();
        }
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (asteroidsGame.isPaused()) return;

        for (Collider collidingObject : collidingObjects) {
            if (collidingObject instanceof Laser laser && !laser.isEnemyLaser()) {
                laser.remove();
                SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
                enemyManager.onUfoDestroyed(this);
                remove();
            } else if (collidingObject instanceof PlayerSpaceship player) {
                SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
                player.destroy();
            }
        }
    }
}
