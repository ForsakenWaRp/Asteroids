# Projectdocumentatie: Asteroids (Java & Yaeger Engine)

Dit document biedt een technisch overzicht van de architectuur, de toegepaste Object-Oriented Programming (OOP) principes en de achterliggende logica van dit Asteroids-project.

---

## 1. Architectuur en Klassenstructuur

Het project is modulair opgezet waarbij gebruik wordt gemaakt van de **Yaeger** game engine. De codebase is onderverdeeld in specifieke domeinen:

*   **`nl.han.asteroids.entities`**: Bevat de domeinobjecten. Hier wordt intensief gebruik gemaakt van overerving (`inheritance`). `BaseEnemy` dient als abstracte basisklasse voor zowel `Asteroid` als `Ufo`, waardoor gedeelde functionaliteit (zoals bewegingslogica) gecentraliseerd is.
*   **`nl.han.asteroids.managers`**: Implementeert het *Manager Pattern*. Deze klassen (zoals `EnemyManager` en `ProjectileManager`) ontkoppelen de creatie van entiteiten van de scènestructuur.
*   **`nl.han.asteroids.config`**: Bevat `GameConstants`, een klasse met `public static final` variabelen voor globale configuratie en magische getallen (magic numbers).

---

## 2. Toegepaste OOP-Principes

### A. Encapsulatie en Toegangsbeheer
Data-integriteit wordt gewaarborgd door het gebruik van `private` en `protected` velden. Toegang tot deze velden verloopt via publieke methoden (getters/setters), wat directe manipulatie van de interne staat van objecten voorkomt.

### B. Polymorfisme en Interfaces
Het project maakt gebruik van interfaces om gedrag te definiëren zonder de implementatie vast te leggen:
*   **`KeyListener`**: Voor de afhandeling van gebruikersinput.
*   **`Collided`**: Definieert het contract voor objecten die kunnen interacteren bij een botsing.
*   **`UpdateExposer`**: Voor logica die per frame moet worden uitgevoerd (de game loop).

### C. State Design Pattern
De klasse `PlayerSpaceship` delegeert gedrag naar een `PlayerState` object. Dit patroon elimineert complexe conditionele logica (`if-else` of `switch` statements) en maakt het mogelijk om de status van de speler (bijv. `InvulnerableState` vs `NormalState`) op runtime te wisselen zonder de hoofdklasse te wijzigen.

---

## 3. Bewegingslogica en Fysica

De beweging in het spel is gebaseerd op vector-gebaseerde fysica, geïmplementeerd in de `applyPhysics()` methode van de speler:

### Versnelling en Lineaire Wrijving
De snelheid wordt berekend aan de hand van een constante versnelling (`acceleration`) en een wrijvingscoëfficiënt (`friction`):
1.  **Acceleratie**: `velocity = velocity + acceleration` (wanneer input wordt gedetecteerd).
2.  **Wrijving (Drag)**: `velocity = velocity * friction` (waarbij `friction < 1.0`).
Dit zorgt voor een asymtotische benadering van de topsnelheid en een natuurlijk vertragingsproces.

### Scherm-Wrapping (Torus-geometrie)
In de `notifyBoundaryCrossing` methode wordt een overgangsberekening uitgevoerd. Wanneer een entiteit de grenzen van de `Coordinate2D` ruimte (het scherm) overschrijdt, wordt de coördinaat vertaald naar de tegenovergestelde zijde. Dit simuleert een continue speelwereld.

---

## 4. Functioneel Programmeren en Callbacks

Het project maakt gebruik van modern Java (Lambdas) voor het afhandelen van gebeurtenissen.
*   **Callbacks**: De `EnemyManager` ontvangt een `Runnable` (functionele interface) via de constructor. Dit stelt de manager in staat om de `GameScene` te informeren over score-wijzigingen zonder een directe afhankelijkheid (dependency) van de UI-logica te hebben.

---

## 5. Uitvoering en Build-proces

Het project wordt beheerd via **Maven** (`pom.xml`).
*   **Dependencies**: De Yaeger engine en Jackson (voor JSON-verwerking) worden automatisch berekend en geïmporteerd.
*   **Entry Point**: De applicatie start in `nl.han.asteroids.App`, die de `launch()` methode van de `YaegerGame` klasse aanroept om de JavaFX-runtime te initialiseren.
