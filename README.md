# Hey Nani hierbij wat documentatie en uitleg voor je! Als je verdere info nodig heb weet je me te vinden.

# 🌌 Asteroids: Architectuur & Implementatie

Welkom bij de technische documentatie van het Asteroids-project. Dit document is bedoeld voor ontwikkelaars die een dieper inzicht willen krijgen in de software-architectuur, de toegepaste Object-Oriented Programming (OOP) principes en de uitbreidingsmogelijkheden van dit systeem.

---

## 🏗️ 1. Modulaire Architectuur

Het project is opgebouwd rondom de **Yaeger Game Engine**, waarbij een strikte scheiding van verantwoordelijkheden (*Separation of Concerns*) is gehanteerd. De codebase is onderverdeeld in logische modules (packages):

| Module | Verantwoordelijkheid |
| :--- | :--- |
| **`config`** | Beheer van globale constanten en configuratie-instellingen ([`GameConstants`](src/main/java/nl/han/asteroids/config/GameConstants.java)). Alles rondom snelheden, spawn-kansen en scores is hier gecentraliseerd. |
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

---

# 🐸 Speciale Nani Editie: Hoe start een Yaeger Game op?

Hé Nani! Omdat ik hoorde dat je bezig wilt gaan met een **Frogger** game (super vet idee trouwens!), heb ik hieronder nog even haarfijn uitgelegd hoe een game in Han Yaeger precies opstart. 

Als je straks je eigen project aanmaakt (bijvoorbeeld `NaniFrogger`), dan zul je altijd met twee hele belangrijke "Start" klassen te maken krijgen. In dit Asteroids project zijn dat `App.java` en `AsteroidsGame.java`. Hier is precies wat ze doen:

### 1. `App.java` (De onzichtbare motor)
Dit is het *Entry Point* van de Java applicatie. Oftewel: hier begint Java met lezen.
```java
public class App {
    public static void main(String[] args) {
        AsteroidsGame.main(args);
    }
}
```
**Wat doet dit en waarom?**
Sinds Java 11 is "JavaFX" (de grafische motor waar Han Yaeger op draait) niet meer standaard ingebouwd in Java. Als we de game direct via de `AsteroidsGame` klasse zouden opstarten, raakt Java in de war en krijg je een foutmelding over ontbrekende modules. 
Door een hele simpele `App` klasse te maken die alléén maar de game doorstart, fopt dit eigenlijk het systeem ("omzeilt een module-check") waardoor de game zonder foutmeldingen opstart.
**Voor jouw Frogger game:** Je maakt straks een `App.java` aan en zet daar letterlijk in: `FroggerGame.main(args);`. Daarna hoef je er nóóit meer naar te kijken!

### 2. `AsteroidsGame.java` (De Regisseur van de Engine)
Dit is het hart van je applicatie. Deze klasse is verplicht om de `YaegerGame` klasse te *extenden*. Voor jou zal dit straks `public class FroggerGame extends YaegerGame` zijn.

Deze klasse doet eigenlijk maar twee hele belangrijke dingen:

**A. `setupGame()`**
Hier stel je de "fysieke" eigenschappen van je gamevenster in.
*   Je roept `setGameTitle("Nani's Frogger")` aan om de titelbalk van het Windows/Mac scherm in te stellen.
*   Je roept `setSize(new Size(800, 600))` aan om te bepalen hoe groot je spel is.

**B. `setupScenes()`**
Han Yaeger werkt met *Scenes* (vergelijk het met dia's of toneelstukken). Je hebt bijvoorbeeld een Hoofdmenu, een Game-scherm en een Game-Over scherm. 
In `setupScenes()` vertel je de regisseur (YaegerGame) welke scènes er allemaal bestaan in jouw spel.
```java
    @Override
    public void setupScenes() {
        addScene(0, new MainMenuScene(this));
        addScene(1, new FroggerGameScene(this)); // Jouw level!
        addScene(2, new GameOverScene(this));
    }
```
Wanneer je later in je game af bent (de kikker is aangereden), kun je aan deze "regisseur" vragen om een andere scène op te zetten via: `setActiveScene(2);`.

**De Interface Tip (`PauseStateProvider`)**
In ons Asteroids project zie je dat deze klasse ook `implements PauseStateProvider` heeft en methodes zoals `isPaused()` bevat. Omdat *álle* scenes (en dus ook vijanden) aan deze game gekoppeld zijn, gebruiken we deze `AsteroidsGame` (of straks `FroggerGame`) als centrale plek om bij te houden of het hele spel op pauze staat en wat de eindscore was.

### 3. Wat is de Han Yaeger Engine eigenlijk?
Voordat we verder de code in duiken: wat is Han Yaeger nou precies? 
Han Yaeger is een **2D Game Engine** geschreven in Java, speciaal gebouwd voor en door studenten/docenten (o.a. van de HAN). Het draait op de achtergrond op **JavaFX**, een framework om grafische applicaties te bouwen. 
Waarom gebruiken we het? Als je in pure JavaFX een game wil maken, moet je zelf een *Game Loop* schrijven (een oneindige loop die 60 keer per seconde het scherm updatet), zelf botsingen berekenen en zelf sprites (afbeeldingen) inladen. Yaeger doet dit allemaal al voor je! Het geeft je kant-en-klare klassen (zoals `YaegerGame`, `DynamicScene`, en `DynamicSpriteEntity`) zodat jij je alleen maar hoeft bezig te houden met het maken van je Frogger levels!

### 4. Wat betekent `extends`?
Wanneer je een nieuwe klasse maakt, zoals `public class FroggerGame extends YaegerGame`, dan maak je gebruik van **Overerving (Inheritance)**. 
Met `extends` zeg je eigenlijk: *"Mijn nieuwe `FroggerGame` is in de basis een `YaegerGame`, maar ik ga er nog mijn eigen functionaliteit (mijn levels en logica) aan toevoegen."* 
Je hoeft de ingewikkelde code voor het opstarten van een JavaFX scherm niet meer zelf te schrijven, want je "erft" al die functionaliteit gratis en voor niets over van de `YaegerGame` klasse uit de engine. Een auto in jouw game zou bijvoorbeeld `public class Auto extends DynamicSpriteEntity` kunnen zijn: je auto kan hierdoor (via de methodes van de superklasse) direct bewegen en op het scherm getekend worden!

### 5. Waarom staat er overal `@Override`?
Je zult zien dat methodes zoals `setupGame()` en `setupScenes()` een `@Override` tag boven zich hebben staan. Wat betekent dat?
*   **Wat doet het?** Omdat jouw klasse overerft van een superklasse (zoals `YaegerGame`), bestaan deze methodes vaak al in de originele engine-code. Door `@Override` te typen, vertel je aan Java: *"Negeer de originele versie van deze methode uit de engine, en gebruik in plaats daarvan mijn eigen, aangepaste versie hieronder!"*
*   **Wanneer is het verplicht?** Overschrijven (overriden) is **verplicht** als de originele klasse een methode **`abstract`** heeft gemaakt (zoals `setupGame()` in Yaeger). Een abstracte methode in Java betekent eigenlijk: "Ik heb dit gedrag bedacht, maar nog niet ingevuld. Iedere klasse die mij `extends` MÓET dit zelf invullen."
*   **Wanneer is het optioneel?** Als de originele klasse al een werkende standaard-implementatie heeft (zoals een `explicitUpdate()` die standaard gewoon niets doet), mag je hem overriden om er zelf functionaliteit aan toe te voegen, maar je bént niet verplicht om de methode in je klasse op te nemen als je hem niet nodig hebt. De `@Override` annotatie is overigens technisch gezien niet verplicht, maar wél heel erg belangrijk: als je een typfout maakt in de methodenaam (bijv. `setupScenees()` in plaats van `setupScenes()`), waarschuwt Java je direct dat je eigenlijk niks aan het overriden bent!

Succes met bouwen! 🚀🐸

