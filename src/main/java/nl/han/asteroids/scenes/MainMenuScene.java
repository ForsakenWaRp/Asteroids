package nl.han.asteroids.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.ui.AsteroidsButton;
import nl.han.asteroids.managers.SoundManager;
import nl.han.asteroids.entities.ui.Star;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import nl.han.asteroids.entities.ui.BackgroundAsteroid;

/**
 * Hallo Nani! Dit is de MainMenuScene. Het allereerste scherm dat je ziet als je de game opstart!
 * Net als GameScene en HighscoreScene erft het van DynamicScene (want de sterren bewegen).
 * Het toont knoppen waarmee je het spel kunt starten of de highscores kunt bekijken.
 * We gebruiken Encapsulatie voor de lijsten en knoppen zodat alleen deze klasse ze aanpast.
 * 
 * Han Yaeger documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class MainMenuScene extends DynamicScene implements KeyListener {

    private final AsteroidsGame asteroidsGame;
    private final List<AsteroidsButton> menuButtons = new ArrayList<>();
    private int selectedIndex = 0;
    private Set<KeyCode> previouslyPressedKeys = new HashSet<>();
    private AsteroidsButton audioButton;

    /**
     * Constructor van het hoofdmenu.
     * @param asteroidsGame De referentie naar de hoofdgame.
     */
    public MainMenuScene(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
    }

    /**
     * @Override: Stelt de scène in en start de epische menu muziek.
     */
    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
        SoundManager.setAudioEnabled(asteroidsGame.isAudioEnabled());
        SoundManager.play(SoundManager.SoundType.MENU_THEME);
    }

    /**
     * @Override: Plaatst de titel, de knoppen en de achtergrond op de scene.
     */
    @Override
    public void setupEntities() {
        menuButtons.clear();
        previouslyPressedKeys.clear();
        setupBackgroundStars();
        setupBackgroundAsteroids();

        var titleText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 3 - 50),
                "ASTEROIDS"
        );
        titleText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        titleText.setFill(Color.YELLOW);
        titleText.setStrokeColor(Color.WHITE);
        titleText.setStrokeWidth(2.0);
        titleText.setFont(Font.font("Roboto", FontWeight.BOLD, 120));
        addEntity(titleText);

        var playButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2),
                "Play Game",
                () -> {
                    SoundManager.stop(SoundManager.SoundType.MENU_THEME);
                    Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_GAME));
                }
        );
        addEntity(playButton);
        menuButtons.add(playButton);

        audioButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 60),
                getAudioText(),
                this::toggleAudio
        );
        addEntity(audioButton);
        menuButtons.add(audioButton);

        var highscoresButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 120),
                "Highscores",
                () -> {
                    Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_HIGHSCORES));
                }
        );
        addEntity(highscoresButton);
        menuButtons.add(highscoresButton);
        
        var instructionsText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT - 50),
                "Pijltjes om te navigeren - Enter om te kiezen"
        );
        instructionsText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        instructionsText.setFill(Color.GRAY);
        instructionsText.setFont(Font.font("Roboto", FontWeight.LIGHT, 20));
        addEntity(instructionsText);

        var signatureText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH - 20, GameConstants.HEIGHT - 20),
                "Made by Johnny"
        );
        signatureText.setAnchorPoint(AnchorPoint.BOTTOM_RIGHT);
        signatureText.setFill(Color.DARKGRAY);
        signatureText.setFont(Font.font("Roboto", FontWeight.LIGHT, 14));
        addEntity(signatureText);

        updateSelection();
    }

    private void setupBackgroundAsteroids() {
        var random = new Random();
        for (int i = 0; i < 5; i++) {
            addEntity(new BackgroundAsteroid(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT)));
        }
    }

    private String getAudioText() {
        return "Audio: " + (asteroidsGame.isAudioEnabled() ? "ON" : "OFF");
    }

    private void toggleAudio() {
        boolean nextState = !asteroidsGame.isAudioEnabled();
        asteroidsGame.setAudioEnabled(nextState);
        SoundManager.setAudioEnabled(nextState);
        audioButton.updateText(getAudioText());
        
        if (nextState) {
            SoundManager.play(SoundManager.SoundType.MENU_THEME);
        }
    }

    private void updateSelection() {
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).setSelected(i == selectedIndex);
        }
    }

    /**
     * @Override: Voor de KeyListener, om met de pijltjestoetsen door het menu te navigeren.
     */
    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.UP) && !previouslyPressedKeys.contains(KeyCode.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) selectedIndex = menuButtons.size() - 1;
            updateSelection();
        } else if (pressedKeys.contains(KeyCode.DOWN) && !previouslyPressedKeys.contains(KeyCode.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuButtons.size()) selectedIndex = 0;
            updateSelection();
        } else if (pressedKeys.contains(KeyCode.ENTER) && !previouslyPressedKeys.contains(KeyCode.ENTER)) {
            if (selectedIndex >= 0 && selectedIndex < menuButtons.size()) {
                menuButtons.get(selectedIndex).onMouseButtonPressed(javafx.scene.input.MouseButton.PRIMARY, null);
            }
        } 
        
        if (menuButtons.get(selectedIndex) == audioButton) {
            if ((pressedKeys.contains(KeyCode.LEFT) && !previouslyPressedKeys.contains(KeyCode.LEFT)) ||
                (pressedKeys.contains(KeyCode.RIGHT) && !previouslyPressedKeys.contains(KeyCode.RIGHT))) {
                toggleAudio();
            }
        }
        
        previouslyPressedKeys = new HashSet<>(pressedKeys);
    }

    private void setupBackgroundStars() {
        var random = new Random();
        for (int layer = 1; layer <= 3; layer++) {
            for (int i = 0; i < 40; i++) {
                addEntity(new Star(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT), layer));
            }
        }
    }
}
