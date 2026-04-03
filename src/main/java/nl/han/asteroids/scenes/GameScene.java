package nl.han.asteroids.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.entities.YaegerEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.entities.player.PlayerSpaceship;
import nl.han.asteroids.managers.*;
import nl.han.asteroids.ui.ScoreText;
import nl.han.asteroids.ui.HealthDisplay;
import nl.han.asteroids.ui.AsteroidsButton;
import nl.han.asteroids.entities.ui.Star;
import nl.han.asteroids.interfaces.EntityAdder;

import java.util.*;

public class GameScene extends DynamicScene implements EntitySpawnerContainer, KeyListener, EntityAdder {

    private final AsteroidsGame asteroidsGame;
    private final ProjectileManager projectileManager;
    private final ParticleManager particleManager;
    private EnemyManager enemyManager;
    private GameScoreManager gameScoreManager;
    private ScoreText scoreText;
    private HealthDisplay healthDisplay;
    private int lives = GameConstants.INITIAL_LIVES;

    private final List<YaegerEntity> pauseMenuEntities = new ArrayList<>();
    private final List<AsteroidsButton> pauseButtons = new ArrayList<>();
    private int selectedPauseIndex = 0;
    private Set<KeyCode> previouslyPressedKeys = new HashSet<>();
    private AsteroidsButton audioButton;

    public GameScene(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
        // ProjectileManager en ParticleManager gebruiken nu de EntityAdder interface
        this.projectileManager = new ProjectileManager(this);
        this.particleManager = new ParticleManager(this);
    }

    @Override
    public void setupScene() {
        setBackgroundColor(GameConstants.BACKGROUND_COLOR);
    }

    @Override
    public void setupEntitySpawners() {
        gameScoreManager = new GameScoreManager(() -> {
            if (scoreText != null) {
                scoreText.setScore(gameScoreManager.getScore(), gameScoreManager.getMultiplier());
            }
        });
        
        enemyManager = new EnemyManager(asteroidsGame, gameScoreManager, projectileManager, particleManager);
        addEntitySpawner(enemyManager);
    }

    @Override
    public void setupEntities() {
        asteroidsGame.setPaused(false);
        pauseMenuEntities.clear();
        pauseButtons.clear();
        previouslyPressedKeys.clear();
        
        setupBackgroundStars();
        lives = GameConstants.INITIAL_LIVES;
        scoreText = new ScoreText(new Coordinate2D(10, 10));
        addEntity(scoreText);

        healthDisplay = new HealthDisplay(this, new Coordinate2D(GameConstants.WIDTH - 10, 10), lives);

        spawnPlayer();
    }

    private void setupBackgroundStars() {
        var random = new Random();
        for (int layer = 1; layer <= 3; layer++) {
            for (int i = 0; i < GameConstants.BACKGROUND_STARS_PER_LAYER; i++) {
                addEntity(new Star(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT), layer));
            }
        }
    }

    private void spawnPlayer() {
        var player = new PlayerSpaceship(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2),
                projectileManager,
                this::onPlayerDeath,
                asteroidsGame
        );
        addEntity(player);
    }

    @Override
    public void addNewEntity(YaegerEntity entity) {
        addEntity(entity);
    }

    private void onPlayerDeath() {
        lives--;
        healthDisplay.setLives(lives);
        
        if (lives > 0) {
            spawnPlayer();
        } else {
            Platform.runLater(() -> {
                asteroidsGame.setLastScore(gameScoreManager.getScore());
                asteroidsGame.setActiveScene(GameConstants.SCENE_GAME_OVER);
            });
        }
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.ESCAPE) && !previouslyPressedKeys.contains(KeyCode.ESCAPE)) {
            togglePause();
        }

        if (asteroidsGame.isPaused()) {
            handlePauseNavigation(pressedKeys);
        }
        
        previouslyPressedKeys = new HashSet<>(pressedKeys);
    }

    private void handlePauseNavigation(Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.UP) && !previouslyPressedKeys.contains(KeyCode.UP)) {
            selectedPauseIndex--;
            if (selectedPauseIndex < 0) selectedPauseIndex = pauseButtons.size() - 1;
            updatePauseSelection();
        } else if (pressedKeys.contains(KeyCode.DOWN) && !previouslyPressedKeys.contains(KeyCode.DOWN)) {
            selectedPauseIndex++;
            if (selectedPauseIndex >= pauseButtons.size()) selectedPauseIndex = 0;
            updatePauseSelection();
        } else if (pressedKeys.contains(KeyCode.ENTER) && !previouslyPressedKeys.contains(KeyCode.ENTER)) {
            if (selectedPauseIndex >= 0 && selectedPauseIndex < pauseButtons.size()) {
                pauseButtons.get(selectedPauseIndex).onMouseButtonPressed(javafx.scene.input.MouseButton.PRIMARY, null);
            }
        }

        if (selectedPauseIndex >= 0 && selectedPauseIndex < pauseButtons.size() && pauseButtons.get(selectedPauseIndex) == audioButton) {
            if ((pressedKeys.contains(KeyCode.LEFT) && !previouslyPressedKeys.contains(KeyCode.LEFT)) ||
                (pressedKeys.contains(KeyCode.RIGHT) && !previouslyPressedKeys.contains(KeyCode.RIGHT))) {
                toggleAudio();
            }
        }
    }

    private void updatePauseSelection() {
        for (int i = 0; i < pauseButtons.size(); i++) {
            pauseButtons.get(i).setSelected(i == selectedPauseIndex);
        }
    }

    private void togglePause() {
        boolean nextState = !asteroidsGame.isPaused();
        asteroidsGame.setPaused(nextState);
        
        if (nextState) {
            showPauseMenu();
        } else {
            hidePauseMenu();
        }
    }

    private void showPauseMenu() {
        enemyManager.pause();
        pauseButtons.clear();
        selectedPauseIndex = 0;
        
        var pauseText = new TextEntity(new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 3 - 50), "PAUSED");
        pauseText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        pauseText.setFill(Color.YELLOW);
        pauseText.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        addEntity(pauseText);
        pauseMenuEntities.add(pauseText);

        var resumeButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2),
                "Resume",
                this::togglePause
        );
        addEntity(resumeButton);
        pauseMenuEntities.add(resumeButton);
        pauseButtons.add(resumeButton);

        audioButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 60),
                getAudioText(),
                this::toggleAudio
        );
        addEntity(audioButton);
        pauseMenuEntities.add(audioButton);
        pauseButtons.add(audioButton);

        var quitButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 120),
                "Quit to Menu",
                () -> {
                    asteroidsGame.setPaused(false);
                    SoundManager.stopAll();
                    Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_MAIN_MENU));
                }
        );
        addEntity(quitButton);
        pauseMenuEntities.add(quitButton);
        pauseButtons.add(quitButton);

        updatePauseSelection();
    }

    private void hidePauseMenu() {
        for (var entity : pauseMenuEntities) {
            entity.remove();
        }
        pauseMenuEntities.clear();
        pauseButtons.clear();
        enemyManager.resume();
    }

    private String getAudioText() {
        return "Audio: " + (asteroidsGame.isAudioEnabled() ? "ON" : "OFF");
    }

    private void toggleAudio() {
        boolean nextState = !asteroidsGame.isAudioEnabled();
        asteroidsGame.setAudioEnabled(nextState);
        SoundManager.setAudioEnabled(nextState);
        audioButton.updateText(getAudioText());
    }
}