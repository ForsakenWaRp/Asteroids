package nl.han.asteroids.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.StaticScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.ScoreManager;
import nl.han.asteroids.models.PlayerScore;
import nl.han.asteroids.ui.AsteroidsButton;

import java.util.*;

/**
 * De scène die wordt getoond wanneer het spel eindigt, nu met toetsenbordbediening.
 */
public class GameOverScene extends StaticScene implements KeyListener {

    private final AsteroidsGame asteroidsGame;
    private boolean scoreSaved = false;
    private final List<AsteroidsButton> menuButtons = new ArrayList<>();
    private int selectedIndex = 0;
    private Set<KeyCode> previouslyPressedKeys = new HashSet<>();

    public GameOverScene(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
        scoreSaved = false;
    }

    @Override
    public void setupEntities() {
        menuButtons.clear();
        previouslyPressedKeys.clear();

        var gameOverText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 3),
                "GAME OVER"
        );
        gameOverText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        gameOverText.setFill(Color.RED);
        gameOverText.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        addEntity(gameOverText);

        int finalScore = asteroidsGame.getLastScore();

        var scoreText = new TextEntity(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 - 50),
                "Final Score: " + finalScore
        );
        scoreText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Roboto", FontWeight.BOLD, 40));
        addEntity(scoreText);

        var saveButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 50),
                "Save Score",
                () -> saveScore(finalScore)
        );
        addEntity(saveButton);
        menuButtons.add(saveButton);

        var menuButton = new AsteroidsButton(
                new Coordinate2D(GameConstants.WIDTH / 2, GameConstants.HEIGHT / 2 + 120),
                "Main Menu",
                () -> Platform.runLater(() -> asteroidsGame.setActiveScene(GameConstants.SCENE_MAIN_MENU))
        );
        addEntity(menuButton);
        menuButtons.add(menuButton);

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

    private void updateSelection() {
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).setSelected(i == selectedIndex);
        }
    }

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
        previouslyPressedKeys = new HashSet<>(pressedKeys);
    }
    
    private void saveScore(int score) {
        if (scoreSaved) return;
        Platform.runLater(() -> {
            String lastUsedName = ScoreManager.getLastUsedName();
            TextInputDialog dialog = new TextInputDialog(lastUsedName);
            dialog.setTitle("Save Highscore");
            dialog.setHeaderText("Enter your name:");
            dialog.setContentText("Name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                ScoreManager.saveScore(new PlayerScore(name, score));
                scoreSaved = true;
                asteroidsGame.setActiveScene(GameConstants.SCENE_HIGHSCORES);
            });
        });
    }
}
