package nl.han.asteroids.ui;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.asteroids.interfaces.EntityAdder;

import java.util.ArrayList;
import java.util.List;

/**
 * Hoi Nani! Deze HealthDisplay klasse laat zien hoeveel levens de speler nog heeft.
 * In plaats van te erven van een Entity, is dit eigenlijk een beheerdersklasse. 
 * Het maakt SpriteEntity objecten (kleine ruimtescheepjes) aan en voegt ze toe via EntityAdder.
 * Hier zie je weer Encapsulatie in actie: de lijst met 'ships' is private, 
 * dus andere klassen kunnen alleen het aantal levens aanpassen via setLives().
 * 
 * Han Yaeger documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class HealthDisplay {
    private final List<SpriteEntity> ships = new ArrayList<>();
    private final EntityAdder adder;
    private final Coordinate2D startLocation;

    /**
     * Constructor voor HealthDisplay.
     * @param adder De klasse (zoals GameScene) die de scheepjes daadwerkelijk op het scherm plaatst.
     * @param startLocation De beginpositie voor de icoontjes.
     * @param initialLives Het aantal startlevens.
     */
    public HealthDisplay(EntityAdder adder, Coordinate2D startLocation, int initialLives) {
        this.adder = adder;
        this.startLocation = startLocation;
        setLives(initialLives);
    }

    private void addShip(int index) {
        var ship = new SpriteEntity(
                "sprites/ship_idle.png",
                new Coordinate2D(startLocation.getX() - (index * 40), startLocation.getY()),
                new Size(32, 24)
        ) {};
        ship.setAnchorPoint(AnchorPoint.TOP_RIGHT);
        adder.addNewEntity(ship);
        ships.add(ship);
    }

    /**
     * Updatet de weergegeven levens op het scherm.
     * @param lives Het nieuwe aantal levens.
     */
    public void setLives(int lives) {
        for (SpriteEntity ship : ships) {
            ship.remove();
        }
        ships.clear();

        for (int i = 0; i < lives; i++) {
            addShip(i);
        }
    }
}
