package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.scenes.SceneBorder;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.EnemyManager;
import nl.han.asteroids.managers.ProjectileManager;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.managers.SoundManager;

import java.util.List;
import java.util.Random;

/**
 * Hey Nani! Deze klasse vertegenwoordigt de UFO vijand.
 * 
 * OOP Principes:
 * - Overerving (Inheritance): Breidt BaseEnemy uit.
 * - Interfaces: Implementeert TimerContainer voor herhalende acties (schieten), en Collider/Collided voor botsingen.
 * 
 * Han Yaeger Engine:
 * - TimerContainer: Laat ons timers toevoegen die na een bepaalde tijd afgaan (bijvoorbeeld om te schieten).
 * - SceneBorderCrossingWatcher: Laat ons reageren als we de rand van het scherm bereiken.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class Ufo extends BaseEnemy implements SceneBorderCrossingWatcher, Collided, Collider, TimerContainer {

    private final ProjectileManager projectileManager;
    private static final long SHOT_INTERVAL = GameConstants.SHOOT_INTERVAL_UFO;
    private final Random random = new Random();
    private boolean soundPaused = false;

    /**
     * Maakt een nieuwe UFO aan.
     * @param initialLocation Startpositie (x, y)
     * @param projectileManager Manager voor het afvuren van lasers
     * @param enemyManager Manager voor alle vijanden
     * @param pauseStateProvider Om te controleren of het spel pauzeert
     */
    public Ufo(Coordinate2D initialLocation, ProjectileManager projectileManager, EnemyManager enemyManager, PauseStateProvider pauseStateProvider) {
        super("sprites/ufo.png", initialLocation, new Size(96, 80), enemyManager, pauseStateProvider);
        this.projectileManager = projectileManager;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setMotion(GameConstants.UFO_SPEED, 90);
        SoundManager.play(SoundManager.SoundType.UFO_ENGINE);
    }

    /**
     * @Override van TimerContainer.
     * Hier definiëren we een timer om de UFO periodiek te laten schieten.
     */
    @Override
    public void setupTimers() {
        addTimer(new Timer(SHOT_INTERVAL) {
            @Override
            public void onAnimationUpdate(long timestamp) {
                if (pauseStateProvider.isPaused()) return;
                
                double direction = random.nextDouble() * 360;
                projectileManager.spawnProjectile(getAnchorLocation(), direction, true);
                SoundManager.play(SoundManager.SoundType.UFO_FIRE);
            }
        });
    }

    /**
     * @Override van BaseEnemy.
     * Zorgt ervoor dat het geluid weer aangaat als we uit pauze komen.
     */
    @Override
    protected void updateEnemy(long timestamp) {
        if (soundPaused) {
            SoundManager.play(SoundManager.SoundType.UFO_ENGINE);
            soundPaused = false;
        }
    }

    /**
     * @Override van de standaard Han Yaeger update loop (UpdateExposer).
     * Controleert elke game-tick of het geluid gepauzeerd moet worden.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        super.explicitUpdate(timestamp);
        
        if (pauseStateProvider.isPaused() && !soundPaused) {
            SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
            soundPaused = true;
        }
    }

    /**
     * @Override van SceneBorderCrossingWatcher.
     * Verwijdert de UFO als deze het scherm aan de rechterkant verlaat.
     */
    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        if (border == SceneBorder.RIGHT) {
            SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
            remove();
        }
    }

    /**
     * @Override van Collided.
     * Afhandeling van botsingen met andere objecten.
     */
    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (pauseStateProvider.isPaused()) return;

        for (Collider collidingObject : collidingObjects) {
            this.onHitBy(collidingObject);
            if (collidingObject instanceof nl.han.asteroids.interfaces.Hittable hittable) {
                hittable.onHitBy(this);
            }
        }
    }

    /**
     * Kijkt of we geraakt worden door een laser.
     */
    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof Laser laser && !laser.isEnemyLaser()) {
            destroy();
        }
    }

    /**
     * Vernietigt de UFO en stopt de geluiden.
     */
    public void destroy() {
        SoundManager.stop(SoundManager.SoundType.UFO_ENGINE);
        enemyManager.onUfoDestroyed(this);
        remove();
    }
}