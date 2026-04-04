package nl.han.asteroids;

/**
 * Dient als het hoofdinvoerpunt van de applicatie.
 * Hoe het werkt in de gamelogica: Start de Asteroids game door de main-methode van AsteroidsGame aan te roepen.
 * OOP-principes: Abstractie (verbergt de complexiteit van het opstarten van de game).
 *
 * Nani ik heb dit zo gedaan omdat vanaf Java11(Wij gebruiken 21) JavaFX niet meer standaard is.
 * Door het op deze manier te doen omzeil ik een check en kan Yaegermeister gewoon starten.
 * Hier begint de hele game eigenlijk, maar die gaat door naar AsteroidsGame.main.
 * De String[] args zijn eigenlijk argumenten die je mee kan sturen met als je een programma start, maar gebruiken wij niet.
 */
public class App {
    public static void main(String[] args) {
        AsteroidsGame.main(args);
    }
}