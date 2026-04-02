package nl.han.asteroids;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.scenes.*;

public class AsteroidsGame extends YaegerGame {

    private int lastScore = 0;
    private boolean isPaused = false;
    private boolean audioEnabled = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setGameTitle("Asteroids Johnny");
        setSize(new Size(GameConstants.WIDTH, GameConstants.HEIGHT));
    }

    @Override
    public void setupScenes() {
        addScene(GameConstants.SCENE_MAIN_MENU, new MainMenuScene(this));
        addScene(GameConstants.SCENE_GAME, new GameScene(this));
        addScene(GameConstants.SCENE_HIGHSCORES, new HighscoreScene(this));
        addScene(GameConstants.SCENE_GAME_OVER, new GameOverScene(this));
    }
    
    public void setLastScore(int score) {
        this.lastScore = score;
    }
    
    public int getLastScore() {
        return lastScore;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }
}
