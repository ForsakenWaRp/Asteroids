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
public class HighscoreScene extends StaticScene implements KeyListener {

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

        var titleText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, 50),
                "Highscores"
        );
        titleText.setAnchorPoint(AnchorPoint.TOP_CENTER);
        titleText.setFill(Color.YELLOW);
        titleText.setFont(Font.font("Roboto", FontWeight.BOLD, 40));
        addEntity(titleText);

        List<PlayerScore> scores = ScoreManager.loadScores();
        int yPos = 120;
        int limit = Math.min(scores.size(), 10);
        
        for (int i = 0; i < limit; i++) {
            PlayerScore s = scores.get(i);
            var scoreText = new TextEntity(
                    new Coordinate2D(GameConstants.WIDTH / 2, yPos),
                    (i + 1) + ". " + s.getName() + " - " + s.getScore()
            );
            scoreText.setAnchorPoint(AnchorPoint.TOP_CENTER);
            scoreText.setFill(Color.WHITE);
            scoreText.setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
            addEntity(scoreText);
            yPos += 40;
        }

        backButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT - 100),
                "Back to Menu",
                () -> Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_MAIN_MENU))
        );
        addEntity(backButton);
        backButton.setSelected(true); // Altijd geselecteerd aangezien het de enige knop is
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
