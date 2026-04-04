package nl.han.asteroids.entities.player.states;

import com.github.hanyaeger.api.entities.Collider;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.Set;

/**
 * Hey Nani! Dit is de Interface voor het State Pattern van de speler.
 * 
 * OOP Principes:
 * - Interface: Dit definieert een "contract". Iedere klasse die `implements PlayerState` gebruikt (zoals BasePlayerState), MOET deze methoden hebben.
 * - State Pattern: Deze interface zorgt ervoor dat het ruimteschip met verschillende toestanden (kwetsbaar/onkwetsbaar) kan werken, zonder dat het schip hoeft te weten in welke specifieke state het zit.
 * 
 * Han Yaeger Engine:
 * - De methoden in deze interface komen overeen met gebeurtenissen uit de engine, zoals toetsenbordinvoer en botsingen.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public interface PlayerState {
    
    /**
     * Verwerkt toetsenbordinvoer.
     * @param pressedKeys De ingedrukte toetsen.
     */
    void handleInput(Set<KeyCode> pressedKeys);
    
    /**
     * Wordt elke game-tick aangeroepen om acties uit te voeren.
     * @param timestamp Huidige tijd.
     */
    void update(long timestamp);
    
    /**
     * Handelt botsingen af.
     * @param collidingObjects Lijst van objecten waar we mee botsen.
     */
    void onCollision(List<Collider> collidingObjects);
    
    /**
     * Wordt aangeroepen als we door iets geraakt worden.
     * @param collider Het object dat ons raakt.
     */
    void onHitBy(Collider collider);
    
    /**
     * Wordt aangeroepen op het moment dat de speler in deze toestand komt.
     */
    void enter();
}