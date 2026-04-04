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
 * Hey Nani! Dit is de EnemyFactory.
 * Hier passen we het **Factory Pattern** toe. In plaats van dat de code overal zelf nieuwe vijanden aanmaakt met 'new',
 * vragen we deze fabriek om dat te doen. Dit zorgt voor **Single Responsibility**: deze klasse weet alles over het maken van vijanden.
 * Ook maken we gebruik van **Dependency Injection** door de benodigde managers in de constructor mee te geven.
 * 
 * Lees hier meer over de Han Yaeger engine:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class EnemyFactory {

    private final EnemyManager enemyManager;
    private final PauseStateProvider pauseStateProvider;
    private final ProjectileManager projectileManager;
    private final Random random = new Random();

    /**
     * De constructor van de EnemyFactory. 
     * Hier injecteren we de afhankelijkheden (Dependency Injection) die nodig zijn om de vijanden aan te maken.
     * 
     * @param enemyManager De manager die de vijanden beheert.
     * @param pauseStateProvider Zorgt ervoor dat vijanden weten of het spel gepauzeerd is.
     * @param projectileManager De manager voor projectielen (nodig voor de UFO).
     */
    public EnemyFactory(EnemyManager enemyManager, PauseStateProvider pauseStateProvider, ProjectileManager projectileManager) {
        this.enemyManager = enemyManager;
        this.pauseStateProvider = pauseStateProvider;
        this.projectileManager = projectileManager;
    }

    /**
     * Maakt een willekeurige asteroïde aan de rand van het scherm.
     * 
     * @return Een nieuwe Asteroid instantie.
     */
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

    /**
     * Maakt een willekeurige UFO aan op een willekeurige hoogte (y-as).
     * 
     * @return Een nieuwe Ufo instantie.
     */
    public Ufo createRandomUfo() {
        double y = random.nextDouble() * GameConstants.HEIGHT;
        return new Ufo(new Coordinate2D(-50, y), projectileManager, enemyManager, pauseStateProvider);
    }

    /**
     * Maakt een asteroïde aan op een specifieke locatie, handig voor als een grote asteroïde in stukken breekt!
     * 
     * @param location De startcoördinaten van de asteroïde.
     * @param size De grootte van de asteroïde (1, 2 of 3).
     * @return Een nieuwe Asteroid instantie.
     */
    public Asteroid createAsteroid(Coordinate2D location, int size) {
        double direction = random.nextDouble() * 360;
        double speed = 2.0 + random.nextDouble() * 3.0;
        return new Asteroid(location, direction, speed, size, enemyManager, pauseStateProvider);
    }
}
