# Hey Nani hierbij wat documentatie en uitleg voor je! Als je verdere info nodig heb weet je me te vinden.

# 🌌 Asteroids: Architectuur & Implementatie

Welkom bij de technische documentatie van het Asteroids-project. Dit document is bedoeld voor ontwikkelaars die een dieper inzicht willen krijgen in de software-architectuur, de toegepaste Object-Oriented Programming (OOP) principes en de uitbreidingsmogelijkheden van dit systeem.

---

## 🏗️ 1. Modulaire Architectuur

Het project is opgebouwd rondom de **Yaeger Game Engine**, waarbij een strikte scheiding van verantwoordelijkheden (*Separation of Concerns*) is gehanteerd. De codebase is onderverdeeld in logische modules (packages):

| Module | Verantwoordelijkheid |
| :--- | :--- |
| **`config`** | Beheer van globale constanten en configuratie-instellingen (`GameConstants`). Alles rondom snelheden, spawn-kansen en scores is hier gecentraliseerd. |
| **`entities`** | Implementatie van domeinobjecten (speler, vijanden, projectielen). |
| **`factories`** | Bevat de `EnemyFactory` voor het centraal instantiëren van vijanden (Factory Pattern). |
| **`managers`** | Afhandeling van object-levenscycli (spawning, scoring, collision delegatie). Let op: elke manager heeft één specifieke taak (Single Responsibility Principle). |
| **`scenes`** | Definitie van de verschillende gamestates (hoofdmenu, gameplay, game-over). |
| **`ui`** | Visuele representatie van data (score, levens, knoppen). |

### Waarom Modulair?
Door deze modulaire opbouw is de code **testbaar**, **onderhoudbaar** en **schaalbaar**. Een wijziging in de `EnemyManager` heeft bijvoorbeeld geen directe impact op de visuele weergave in de `GameScene`.

---

## 🧬 2. Kernconcepten van OOP & Recente Verbeteringen

Naast de basisconcepten zijn er recent een aantal geavanceerde OOP-patronen en best practices doorgevoerd om de codebase te professionaliseren:

### A. Abstractie, Overerving & Interfaces
Het project maakt intensief gebruik van abstracte basisklassen en interfaces.
*   **`BaseEnemy`**: Centraliseert gedeelde logica (zoals pauzeren). `Asteroid` en `Ufo` erven hiervan.
*   **`PauseStateProvider` (Law of Demeter & Dependency Injection)**: In plaats van de hele `AsteroidsGame` instantie door te geven aan entiteiten om de pauze-status te controleren, is er een specifieke interface gemaakt. Entiteiten weten nu alleen dát er gepauzeerd kan worden, maar niet hoe de rest van de game werkt (losse koppeling).
*   **`Hittable` Interface (Tell, Don't Ask principe)**: Het collision-systeem maakt geen gebruik meer van zware `instanceof` checks waarbij entiteiten andermans data aanpassen. Als objecten botsen roepen ze nu op elkaar de `onHitBy()` methode aan. Het object zélf bepaalt dan wat er gebeurt (bijvoorbeeld levens verliezen of ontploffen).

### B. Design Patterns
*   **State Pattern**: Bestuurt het ruimteschip (`NormalState` vs `InvulnerableState`). Nieuwe statussen kunnen makkelijk worden toegevoegd.
*   **Factory Pattern (`EnemyFactory`)**: De logica voor het bepalen wélke vijand er spawnt en wáár, is uit de `EnemyManager` gehaald. De manager vraagt nu aan de fabriek om een vijand te bouwen, wat de code veel schoner en makkelijker uitbreidbaar maakt.

### C. Moderne Java Features & Data Management
*   **Java 21 Records**: De `PlayerScore` klasse is omgezet naar een `record`, wat resulteert in één regel code in plaats van een grote klasse vol getters en setters.
*   **Java Preferences API**: Highscores worden niet meer weggeschreven als losse `highscores.json` of `.txt` bestanden in de projectmap. De `ScoreManager` maakt nu gebruik van de native `Preferences` API om deze onzichtbaar en veilig in het OS op te slaan.

### D. Single Responsibility & Yaeger Timers
*   **ScoreManager vs EnemyManager**: De score- en combo-logica is uit de `EnemyManager` gehaald en in een eigen `GameScoreManager` geplaatst. 
*   **Yaeger `TimerContainer`**: Hardcoded `System.currentTimeMillis()` berekeningen voor cooldowns (zoals schieten) zijn vervangen door native Yaeger `Timer` objecten via de `TimerContainer` interface. Dit sluit beter aan op de gameloop en pauzeert automatisch mee met het spel.

---

## 🛠️ 3. Het State Design Pattern

De besturing van de speler wordt afgehandeld door het **State Pattern**. De `PlayerSpaceship` delegeert zijn gedrag naar een actieve `PlayerState`.

*   **`NormalState`**: Standaard gameplay waarbij botsingen dodelijk zijn.
*   **`InvulnerableState`**: Tijdelijke status na een spawn waarbij de speler immuun is voor schade.

**Voordeel:** Nieuwe spel-modi (zoals een 'Hyperdrive' of 'Shield' status) kunnen worden toegevoegd door simpelweg een nieuwe klasse te maken die `PlayerState` implementeert, zonder de bestaande `PlayerSpaceship` code te vervuilen met extra `if`-statements.

---

## 🎨 4. Media Assets & Resource Management

### A. Sprites en Visuele Assets
De visuele elementen van het spel worden ingeladen via de `DynamicSpriteEntity` klasse van de Yaeger engine.
*   **Implementatie**: In klassen zoals `PlayerSpaceship` en `Asteroid` wordt het pad naar de sprite (bijv. `"sprites/ship_idle.png"`) direct aan de constructor meegegeven. Yaeger handelt de caching en rendering van deze afbeeldingen intern af.
*   **Herkomst**: De gebruikte sprites zijn verzameld via publieke bronnen op internet (Google) en zijn vrij beschikbaar voor educatief en niet-commercieel gebruik.

### B. Audiobeheer via de `SoundManager`
Het geluid in de game wordt gecentraliseerd beheerd door de `SoundManager` klasse. Dit is een statische utility-klasse die gebruikmaakt van de `SoundClip` API van Yaeger.
*   **Enum-gestuurd**: De `SoundType` enum definieert de beschikbare geluidseffecten (zoals `PLAYER_FIRE` en `ASTEROID_EXPLOSION`) inclusief hun bestandspaden.
*   **Lifecycle**: Geluiden zoals het thema in het hoofdmenu en de UFO-motor worden gepauzeerd of gestopt op basis van de gamestate, wat essentieel is voor een goede gebruikerservaring.

---

## ⚙️ 5. Yaeger Game Engine Integratie

Dit project leunt zwaar op de Yaeger Engine, een JavaFX-gebaseerd framework ontworpen voor het versnellen van game-ontwikkeling. De volgende kernobjecten worden intensief gebruikt:

| Yaeger Object | Toepassing in dit Project |
| :--- | :--- |
| **`YaegerGame`** | De centrale controller (`AsteroidsGame`) die de scènes beheert en de engine initialiseert. |
| **`DynamicScene`** | Gebruikt voor `GameScene`; maakt real-time updates en dynamische interactie mogelijk. |
| **`DynamicSpriteEntity`** | De basis voor alle bewegende objecten. Het biedt ingebouwde methoden voor snelheid, richting en rotatie. |
| **`EntitySpawner`** | Gebruikt in `EnemyManager` om op gezette tijden nieuwe objecten in de wereld te introduceren zonder de hoofd-gameloop te belasten. |
| **`Collider` / `Collided`** | Interfaces die zorgen voor de automatische detectie van botsingen tussen verschillende entiteiten. |

---

## 📐 6. Fysica en Bewegingslogica

De beweging in Asteroids is gebaseerd op een vereenvoudigd vector-model:

1.  **Versnelling (Acceleration)**: Bij input wordt de snelheidsvector verhoogd met een constante factor (`PLAYER_ACCELERATION`).
2.  **Lineaire Wrijving (Drag)**: Elke frame wordt de snelheid vermenigvuldigd met een factor (`PLAYER_FRICTION`, bijv. 0.992). Dit simuleert weerstand, waardoor het schip uiteindelijk tot stilstand komt.
3.  **Torus-geometrie (Wrap-around)**: Omdat de speelwereld begrensd is, worden entiteiten die de schermrand passeren wiskundig vertaald naar de tegenoverliggende zijde (`x = 0` wordt `x = WIDTH`).

---

## 🚀 5. Uitbreidingsgids (De OOP Methode)

Wil je het spel uitbreiden? Gebruik dan de bestaande architectuur als fundament:

### Nieuwe Vijand Toevoegen
1.  Maak een nieuwe klasse aan in `entities.enemy`.
2.  Laat deze klasse `BaseEnemy` **extenden**.
3.  Implementeer de methode `updateEnemy()` voor uniek gedrag (bijvoorbeeld een vijand die de speler achtervolgt).
4.  Registreer de nieuwe vijand in de `EnemyManager` zodat deze gespawnd kan worden.

### Nieuwe Power-up Implementeren
1.  Definieer een nieuwe state in `entities.player.states`.
2.  Erf over van `BasePlayerState`.
3.  Pas de `onCollision()` of `handleInput()` methode aan voor de specifieke power-up logica.
4.  Wissel naar deze state in het `PlayerSpaceship` object wanneer een power-up wordt opgepakt.

---

## 🔧 6. Ontwikkeling & Build
Dit project wordt beheerd met **Maven**.
*   **Entry Point**: `nl.han.asteroids.App`
*   **Hoofdconfiguratie**: `nl.han.asteroids.config.GameConstants`

---
*Documentatie opgesteld voor educatieve doeleinden. Succes met de verdere ontwikkeling!*
