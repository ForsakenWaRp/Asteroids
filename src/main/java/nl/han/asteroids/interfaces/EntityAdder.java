package nl.han.asteroids.interfaces;

import com.github.hanyaeger.api.entities.YaegerEntity;

/**
 * Hey Nani! Dit is de EntityAdder interface.
 * Deze interface maakt gebruik van het **Callback / Observer patroon**.
 * We gebruiken dit om nieuwe entiteiten (zoals kogels of deeltjes) toe te voegen aan de GameScene, 
 * zonder dat de klassen die deze entiteiten maken de hele GameScene hoeven te kennen. Dit is een vorm van **Dependency Inversion**.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public interface EntityAdder {
    /**
     * Voegt een nieuwe YaegerEntity toe aan het spel.
     * 
     * @param entity De entiteit die we aan het spel willen toevoegen.
     */
    void addNewEntity(YaegerEntity entity);
}
