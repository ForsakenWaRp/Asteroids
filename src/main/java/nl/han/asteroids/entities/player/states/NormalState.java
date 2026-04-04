package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.entities.projectiles.Laser;
import nl.han.asteroids.entities.enemy.BaseEnemy;
import nl.han.asteroids.interfaces.Hittable;
import java.util.List;

/**
 * Hey Nani! Dit is de toestand (State) waarin de speler kwetsbaar is.
 * 
 * OOP Principes:
 * - State Pattern: Dit is onderdeel van het State Pattern. De speler gedraagt zich anders in de NormalState (kan doodgaan) dan in de InvulnerableState (onschendbaar).
 * - Overerving: Breidt BasePlayerState uit.
 * 
 * Han Yaeger Engine:
 * - Deze klasse implementeert geen directe Han Yaeger interfaces, maar wordt gebruikt binnen de speler (DynamicSpriteEntity) om botsingen af te handelen.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class NormalState extends BasePlayerState {
    
    /**
     * Maakt de NormalState aan voor een specifieke speler.
     * @param player De speler (PlayerSpaceship) waar deze state bij hoort.
     */
    public NormalState(PlayerSpaceship player) {
        super(player);
    }

    /**
     * @Override van BasePlayerState.
     * Wordt aangeroepen wanneer de speler deze toestand binnengaat. We maken de speler weer volledig zichtbaar.
     */
    @Override
    public void enter() {
        player.setOpacity(1.0);
    }

    /**
     * @Override van BasePlayerState.
     * Verwerkt de botsingen (uit de Collided interface via de speler).
     */
    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider col : collidingObjects) {
            player.onHitBy(col);
            if (col instanceof Hittable hittable) {
                hittable.onHitBy(player);
            }
        }
    }

    /**
     * @Override van BasePlayerState.
     * Als de speler in NormalState wordt geraakt door een vijand of vijandige laser, gaat hij kapot!
     */
    @Override
    public void onHitBy(Collider collider) {
        if (collider instanceof BaseEnemy) {
            player.destroy();
        } else if (collider instanceof Laser laser && laser.isEnemyLaser()) {
            player.destroy();
        }
    }
}