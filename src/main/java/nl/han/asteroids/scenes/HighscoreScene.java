package nl.han.asteroids.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.ScoreManager;
import nl.han.asteroids.models.PlayerScore;
import nl.han.asteroids.ui.AsteroidsButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Toont de top highscores met toetsenbordondersteuning.
 */
import com.github.hanyaeger.api.scenes.DynamicScene;
import nl.han.asteroids.entities.ui.BackgroundAsteroid;
import nl.han.asteroids.entities.ui.Star;
import java.util.Random;

/**
 * Toont de top highscores met toetsenbordondersteuning en visuele flair.
 */
public class HighscoreScene extends DynamicScene implements KeyListener {

    private final AsteroidsGame asteroidsGame;
    private AsteroidsButton backButton;
    private Set<KeyCode> previouslyPressedKeys = new HashSet<>();

    public HighscoreScene(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {
        previouslyPressedKeys.clear();
        setupBackgroundStars();
        setupBackgroundAsteroids();

        var titleText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, 50),
                "HIGHSCORES"
        );
        titleText.setAnchorPoint(AnchorPoint.TOP_CENTER);
        titleText.setFill(Color.YELLOW);
        titleText.setStrokeColor(Color.WHITE);
        titleText.setStrokeWidth(1.0);
        titleText.setFont(Font.font("Roboto", FontWeight.BOLD, 60));
        addEntity(titleText);

        List<PlayerScore> scores = ScoreManager.loadScores();
        int yPos = 150;
        int limit = Math.min(scores.size(), 10);
        
        for (int i = 0; i < limit; i++) {
            PlayerScore s = scores.get(i);
            var scoreText = new TextEntity(
                    new Coordinate2D(GameConstants.WIDTH / 2, yPos),
                    String.format("%2d.  %-15s  %6d", (i + 1), s.name(), s.score())
            );
            scoreText.setAnchorPoint(AnchorPoint.TOP_CENTER);
            scoreText.setFill(i == 0 ? Color.GOLD : i == 1 ? Color.SILVER : i == 2 ? Color.BROWN : Color.WHITE);
            scoreText.setFont(Font.font("Monospaced", FontWeight.BOLD, 24));
            addEntity(scoreText);
            yPos += 45;
        }

        backButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT - 100),
                "Back to Menu",
                () -> Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_MAIN_MENU))
        );
        addEntity(backButton);
        backButton.setSelected(true);
    }

    private void setupBackgroundStars() {
        var random = new Random();
        for (int layer = 1; layer <= 3; layer++) {
            for (int i = 0; i < 30; i++) {
                addEntity(new Star(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT), layer));
            }
        }
    }

    private void setupBackgroundAsteroids() {
        var random = new Random();
        for (int i = 0; i < 3; i++) {
            addEntity(new BackgroundAsteroid(new Coordinate2D(random.nextDouble() * GameConstants.WIDTH, random.nextDouble() * GameConstants.HEIGHT)));
        }
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if ((pressedKeys.contains(KeyCode.ENTER) && !previouslyPressedKeys.contains(KeyCode.ENTER)) ||
            (pressedKeys.contains(KeyCode.ESCAPE) && !previouslyPressedKeys.contains(KeyCode.ESCAPE))) {
            backButton.onMouseButtonPressed(javafx.scene.input.MouseButton.PRIMARY, null);
        }
        previouslyPressedKeys = new HashSet<>(pressedKeys);
    }
}
