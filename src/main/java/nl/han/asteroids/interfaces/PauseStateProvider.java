package nl.han.asteroids.interfaces;

/**
 * Hey Nani, let op! Dit is de PauseStateProvider interface.
 * Deze interface levert de pauze-status via **Dependency Injection**.
 * Dit is een prachtig voorbeeld van de **Law of Demeter** en **Interface Segregation**: in plaats van de hele 
 * GameScene aan vijanden te geven (zodat ze kunnen zien of de game gepauzeerd is), geven we ze alleen
 * dit kleine interfaceje. Zo weten ze precies genoeg, en niet te veel!
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public interface PauseStateProvider {
    /**
     * Controleert of het spel momenteel gepauzeerd is.
     * 
     * @return true als het spel gepauzeerd is, anders false.
     */
    boolean isPaused();
}
