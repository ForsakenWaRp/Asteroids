package nl.han.asteroids.interfaces;

/**
 * Interface die de pauze-status levert voor dependency injection, 
 * volgens de Law of Demeter.
 */
public interface PauseStateProvider {
    boolean isPaused();
}
