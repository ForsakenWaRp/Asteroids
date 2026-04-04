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
 * Hallo Nani! Dit is de GameOverScene. Deze klasse regelt wat je ziet als het spel afgelopen is.
 * Omdat we dit op het scherm willen tonen, erft (Inheritance/extends) het van StaticScene uit de Han Yaeger engine.
 * Een StaticScene beweegt niet vanzelf, perfect voor een menuutje! 
 * We implementeren ook KeyListener, wat betekent dat we beloven (via een interface) dat we naar het toetsenbord luisteren.
 * Encapsulatie passen we toe door onze variabelen (zoals scoreSaved) private te maken, zodat andere klassen hier niet zomaar mee kunnen rommelen!
 * 
 * Voor meer info over de Han Yaeger engine, zie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class GameOverScene extends StaticScene implements KeyListener {

    private final AsteroidsGame asteroidsGame;
    private boolean scoreSaved = false;
    private final List<AsteroidsButton> menuButtons = new ArrayList<>();
    private int selectedIndex = 0;
    private Set<KeyCode> previouslyPressedKeys = new HashSet<>();

    /**
     * De constructor van GameOverScene.
     * @param asteroidsGame De hoofdgame waar we naar terug kunnen refereren. Dit is een mooi voorbeeld van object compositie!
     */
    public GameOverScene(AsteroidsGame asteroidsGame) {
        this.asteroidsGame = asteroidsGame;
    }

    /**
     * @Override betekent dat we een methode van de parent class (StaticScene) overschrijven met onze eigen logica.
     * Kijk Nani, hier stellen we de basis in, zoals de achtergrondkleur van deze scene.
     */
    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
        scoreSaved = false;
    }

    /**
     * @Override: Hier bepalen we welke Entities (zoals tekst en knoppen) op deze Scene worden geplaatst.
     * We maken hier nieuwe objecten aan met 'new' en voegen ze toe aan de scene met addEntity().
     */
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

    /**
     * @Override: Deze methode komt van de KeyListener interface. Hier verwerken we welke toetsen je indrukt.
     * Handig voor in jouw Frogger game Nani, als je de kikker wilt laten springen!
     * @param pressedKeys De set met toetsen die op dit moment zijn ingedrukt.
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
