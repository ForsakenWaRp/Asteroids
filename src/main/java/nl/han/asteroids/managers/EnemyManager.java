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

/**
 * Hey Nani! De EnemyManager regelt het spawnen (tevoorschijn laten komen) van vijanden.
 * Deze klasse breidt de Han Yaeger **EntitySpawner** uit. Het gebruikt het **Single Responsibility Principle**
 * doordat de manager de spawn logica en puntentelling coördineert, terwijl de EnemyFactory de vijanden bouwt.
 * 
 * Meer over EntitySpawner vind je hier:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class EnemyManager extends EntitySpawner {

    private final PauseStateProvider pauseStateProvider;
    private final ProjectileManager projectileManager;
    private final ParticleManager particleManager;
    private final GameScoreManager gameScoreManager;
    private final EnemyFactory enemyFactory;
    private final Random random = new Random();

    /**
     * Constructor voor de EnemyManager met **Dependency Injection** voor alle benodigde componenten.
     * 
     * @param pauseStateProvider Leest of het spel gepauzeerd is.
     * @param gameScoreManager Houdt de score bij.
     * @param projectileManager Beheert de projectielen (nodig voor UFO's).
     * @param particleManager Beheert de deeltjes-effecten zoals explosies.
     */
    public EnemyManager(PauseStateProvider pauseStateProvider, GameScoreManager gameScoreManager, ProjectileManager projectileManager, ParticleManager particleManager) {
        super(GameConstants.INITIAL_SPAWN_INTERVAL);
        this.pauseStateProvider = pauseStateProvider;
        this.gameScoreManager = gameScoreManager;
        this.projectileManager = projectileManager;
        this.particleManager = particleManager;
        this.enemyFactory = new EnemyFactory(this, pauseStateProvider, projectileManager);
    }

    /**
     * Deze methode overschrijft (override) de spawnEntities methode van de EntitySpawner uit Han Yaeger.
     * Met de @Override annotatie geven we aan de compiler (en onszelf) aan dat we een methode uit de ouder-klasse 
     * vervangen met onze eigen logica. Hier bepalen we wanneer en welke vijanden er verschijnen.
     */
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
    
    /**
     * Spawnt een specifieke asteroïde op een gegeven locatie (bijvoorbeeld als een grote in stukken breekt).
     * 
     * @param location De locatie waar de asteroïde moet verschijnen.
     * @param size De grootte van de nieuwe asteroïde.
     */
    public void spawnAsteroid(Coordinate2D location, int size) {
        spawn(enemyFactory.createAsteroid(location, size));
    }

    /**
     * Wordt aangeroepen wanneer een asteroïde is vernietigd.
     * 
     * @param asteroid De asteroïde die zojuist is vernietigd.
     */
    public void onAsteroidDestroyed(Asteroid asteroid) {
        gameScoreManager.addScore((4 - asteroid.getSize()) * GameConstants.ASTEROID_BASE_SCORE);
        particleManager.spawnExplosion(asteroid.getAnchorLocation(), Color.LIGHTGRAY, GameConstants.PARTICLES_PER_ASTEROID_EXPLOSION);
    }

    /**
     * Wordt aangeroepen wanneer een UFO is vernietigd.
     * 
     * @param ufo De UFO die zojuist is vernietigd.
     */
    public void onUfoDestroyed(Ufo ufo) {
        gameScoreManager.addScore(GameConstants.UFO_SCORE);
        particleManager.spawnExplosion(ufo.getAnchorLocation(), Color.ORANGE, GameConstants.PARTICLES_PER_UFO_EXPLOSION);
    }
}
