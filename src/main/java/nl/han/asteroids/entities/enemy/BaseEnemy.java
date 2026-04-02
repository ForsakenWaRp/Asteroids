package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.managers.EnemyManager;

public abstract class BaseEnemy extends DynamicSpriteEntity implements UpdateExposer {
    protected final EnemyManager enemyManager;
    protected final AsteroidsGame asteroidsGame;
    private double savedSpeed = 0;

    public BaseEnemy(String resource, Coordinate2D initialLocation, Size size, EnemyManager enemyManager, AsteroidsGame asteroidsGame) {
        super(resource, initialLocation, size);
        this.enemyManager = enemyManager;
        this.asteroidsGame = asteroidsGame;
    }

    @Override
    public void explicitUpdate(long timestamp) {
        if (asteroidsGame.isPaused()) {
            if (getSpeed() > 0) {
                savedSpeed = getSpeed();
                setSpeed(0);
            }
        } else {
            if (savedSpeed > 0) {
                setSpeed(savedSpeed);
                savedSpeed = 0;
            }
            updateEnemy(timestamp);
        }
    }

    protected abstract void updateEnemy(long timestamp);
}
