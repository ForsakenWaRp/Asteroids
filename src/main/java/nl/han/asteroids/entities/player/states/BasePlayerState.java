package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import javafx.scene.input.KeyCode;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import java.util.List;
import java.util.Set;

/**
 * Hey Nani! Dit is de gedeelde basislogica voor alle speler states.
 * 
 * OOP Principes:
 * - Abstracte Klasse: Omdat niet alles is ingevuld (zoals `onCollision` en `onHitBy`), is deze klasse abstract.
 * - Overerving: Andere states (zoals NormalState en InvulnerableState) "extends" deze klasse en krijgen zo automatisch de besturing (handleInput) en physics (update).
 * - Interfaces: Implementeert `PlayerState`.
 * - State Pattern: Dient als basis voor verschillende toestanden van het schip.
 * 
 * Han Yaeger Engine:
 * - Geen directe koppeling met Han Yaeger API-interfaces in deze specifieke klasse, maar het verwerkt wel KeyCode (invoer) en Collider (botsingen).
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public abstract class BasePlayerState implements PlayerState {
   
    protected final PlayerSpaceship player;

    /**
     * De constructor die door subklassen wordt aangeroepen.
     * @param player De speler waaraan deze state gekoppeld is.
     */
    public BasePlayerState(PlayerSpaceship player) {
        this.player = player;
    }

    /**
     * @Override van PlayerState.
     * Leest de ingedrukte toetsen en vertelt het schip of het moet draaien, versnellen of schieten.
     * @param pressedKeys De ingedrukte toetsen.
     */
    @Override
    public void handleInput(Set<KeyCode> pressedKeys) {
        player.setRotatingLeft(pressedKeys.contains(KeyCode.LEFT));
        player.setRotatingRight(pressedKeys.contains(KeyCode.RIGHT));
        player.setAccelerating(pressedKeys.contains(KeyCode.UP));

        if (pressedKeys.contains(KeyCode.SPACE)) {
            player.handleShooting();
        }
    }

    /**
     * @Override van PlayerState.
     * Past elke game-tick de physics van het schip toe.
     */
    @Override
    public void update(long timestamp) {
        player.applyPhysics();
    }

    /**
     * Abstracte methoden uit PlayerState die subklassen zelf moeten invullen.
     */
    @Override
    public abstract void onCollision(List<Collider> collidingObjects);

    @Override
    public abstract void onHitBy(Collider collider);

    /**
     * Optionele methode die wordt uitgevoerd als de speler deze toestand binnengaat.
     */
    @Override
    public void enter() {}
}