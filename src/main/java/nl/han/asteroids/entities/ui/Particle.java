package nl.han.asteroids.entities.ui;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Hey Nani! Dit is een particle (klein deeltje) voor explosie-effecten.
 * 
 * OOP Principes:
 * - Overerving: Breidt DynamicCircleEntity uit om een bewegende cirkel te zijn.
 * - Interfaces: Implementeert UpdateExposer zodat elk deeltje zichzelf langzaam kan laten vervagen.
 * 
 * Han Yaeger Engine:
 * - DynamicCircleEntity: Een bewegende grafische cirkel.
 * - UpdateExposer: Een interface met de `explicitUpdate` methode, die elke game-tick wordt aangeroepen door de engine.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class Particle extends DynamicCircleEntity implements UpdateExposer {

    private double opacity = 1.0;
    private final double fadeSpeed;

    /**
     * Constructor voor een explosie deeltje.
     * @param initialLocation Startpositie.
     * @param color De kleur van het deeltje.
     */
    public Particle(Coordinate2D initialLocation, Color color) {
        super(initialLocation);
        var random = new Random();
        
        setRadius(1 + random.nextDouble() * 2);
        setFill(color);
        
        double direction = random.nextDouble() * 360;
        double speed = 1.0 + random.nextDouble() * 3.0;
        setMotion(speed, direction);
        
        this.fadeSpeed = 0.01 + random.nextDouble() * 0.02;
    }

    /**
     * @Override van UpdateExposer.
     * Elke tick (heel klein momentje) verlagen we de zichtbaarheid (opacity).
     * Als het deeltje onzichtbaar is, verwijderen we het uit het spel (`remove()`).
     * @param timestamp De huidige tijd.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        opacity -= fadeSpeed;
        if (opacity <= 0) {
            remove();
        } else {
            setOpacity(opacity);
        }
    }
}