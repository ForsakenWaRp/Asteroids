package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import java.util.List;

/**
 * Hey Nani! Dit is de toestand (State) waarin de speler tijdelijk onkwetsbaar is na respawn.
 * 
 * OOP Principes:
 * - State Pattern: Door states in aparte klassen te zetten, is de code van de speler overzichtelijk. De speler gedraagt zich in deze state compleet anders (negeert botsingen).
 * - Overerving: Breidt BasePlayerState uit.
 * 
 * Han Yaeger Engine:
 * - Wordt niet direct door Han Yaeger aangeroepen, maar beheert hoe de speler met de engine (botsingen via Collided/Collider) omgaat.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class InvulnerableState extends BasePlayerState {

    /**
     * Constructor voor de onkwetsbare toestand.
     * @param player Het schip waar deze toestand bij hoort.
     */
    public InvulnerableState(PlayerSpaceship player) {
        super(player);
    }

    /**
     * @Override van BasePlayerState.
     * Wanneer we onkwetsbaar worden, maken we het schip half doorzichtig.
     */
    @Override
    public void enter() {
        player.setOpacity(0.5);
    }

    /**
     * @Override van BasePlayerState.
     * Hier doen we expres NIETS. We negeren alle botsingen omdat we onsterfelijk zijn!
     */
    @Override
    public void onCollision(List<Collider> collidingObjects) {
        // Negeer botsingen tijdens onsterfelijkheid
    }

    /**
     * @Override van BasePlayerState.
     * We negeren ook alle directe hits.
     */
    @Override
    public void onHitBy(Collider collider) {
        // Negeer hits tijdens onsterfelijkheid
    }
}