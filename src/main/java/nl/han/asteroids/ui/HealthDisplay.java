package nl.han.asteroids.ui;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.asteroids.interfaces.EntityAdder;

import java.util.ArrayList;
import java.util.List;

public class HealthDisplay {
    private final List<SpriteEntity> ships = new ArrayList<>();
    private final EntityAdder adder;
    private final Coordinate2D startLocation;

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
