package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.Size;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.managers.EnemyManager;
import nl.han.asteroids.managers.SoundManager;
import nl.han.asteroids.entities.player.PlayerSpaceship;

import java.util.List;
import java.util.Random;

public class Asteroid extends BaseEnemy implements SceneBorderCrossingWatcher, Collided, Collider {
    private final int size;

    public Asteroid(Coordinate2D initialLocation, double direction, double speed, int size, EnemyManager enemyManager, AsteroidsGame asteroidsGame) {
        super(getRandomSprite(size), initialLocation, getActualSize(size), enemyManager, asteroidsGame);
        this.size = size;
        setMotion(speed, direction);
    }

    private static String getRandomSprite(int size) {
        int v = new Random().nextInt(3) + 1;
        String s = (size == 3) ? "large" : (size == 2) ? "medium" : "small";
        return "sprites/asteroid_" + s + "_" + v + ".png";
    }

    private static Size getActualSize(int size) {
        return (size == 3) ? new Size(160, 160) : (size == 2) ? new Size(96, 96) : new Size(64, 64);
    }

    @Override
    protected void updateEnemy(long timestamp) {}

    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        double x = getAnchorLocation().getX();
        double y = getAnchorLocation().getY();
        if (border == SceneBorder.LEFT) x = GameConstants.WIDTH;
        else if (border == SceneBorder.RIGHT) x = 0;
        else if (border == SceneBorder.TOP) y = GameConstants.HEIGHT;
        else if (border == SceneBorder.BOTTOM) y = 0;
        setAnchorLocation(new Coordinate2D(x, y));
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (asteroidsGame.isPaused()) return;
        for (Collider col : collidingObjects) {
            if (col instanceof Laser laser && !laser.isEnemyLaser()) {
                laser.remove();
                destroy();
            } else if (col instanceof PlayerSpaceship player) {
                player.destroy();
            }
        }
    }

    private void destroy() {
        SoundManager.play(SoundManager.SoundType.ASTEROID_EXPLOSION);
        enemyManager.onAsteroidDestroyed(this);
        remove();
        if (size > 1) {
            enemyManager.spawnAsteroid(getAnchorLocation(), size - 1);
            enemyManager.spawnAsteroid(getAnchorLocation(), size - 1);
        }
    }

    public int getSize() { return size; }
}
