package nl.han.asteroids.entities.enemy;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.asteroids.interfaces.Hittable;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.managers.EnemyManager;

/**
 * Hey Nani! Dit is de basisklasse voor alle vijanden.
 * 
 * OOP Principes:
 * - Abstracte Klasse: Deze klasse kan niet direct worden ge(instancieerd), maar dient als blauwdruk voor `Asteroid` en `Ufo`.
 * - Overerving: Breidt DynamicSpriteEntity uit. Alle specifieke vijanden erven (extends) deze BaseEnemy en krijgen zo deze logica.
 * - Interfaces: Implementeert UpdateExposer (voor de game loop) en Hittable (voor botsingen).
 * 
 * Han Yaeger Engine:
 * - DynamicSpriteEntity: Zorgt dat vijanden als bewegende plaatjes in de engine bestaan.
 * - UpdateExposer: Laat vijanden elke tick hun positie of gedrag updaten.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public abstract class BaseEnemy extends DynamicSpriteEntity implements UpdateExposer, Hittable {
    protected final EnemyManager enemyManager;
    protected final PauseStateProvider pauseStateProvider;
    private double savedSpeed = 0;

    /**
     * Constructor die door de subklassen (Asteroid, Ufo) wordt aangeroepen met `super()`.
     * @param resource Het pad naar de sprite (afbeelding).
     * @param initialLocation Startpositie.
     * @param size De afmetingen van de vijand.
     * @param enemyManager De manager.
     * @param pauseStateProvider Pause-status.
     */
    public BaseEnemy(String resource, Coordinate2D initialLocation, Size size, EnemyManager enemyManager, PauseStateProvider pauseStateProvider) {
        super(resource, initialLocation, size);
        this.enemyManager = enemyManager;
        this.pauseStateProvider = pauseStateProvider;
    }

    /**
     * @Override van UpdateExposer.
     * Dit zorgt ervoor dat vijanden stoppen met bewegen als het spel gepauzeerd is.
     * Als het niet gepauzeerd is, roept het de abstracte methode updateEnemy aan.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        if (pauseStateProvider.isPaused()) {
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

    /**
     * Abstracte methode! Dit betekent dat elke vijand die BaseEnemy 'extends', zelf moet bepalen wat er hier gebeurt.
     * @param timestamp De huidige tijd in de game loop.
     */
    protected abstract void updateEnemy(long timestamp);
}