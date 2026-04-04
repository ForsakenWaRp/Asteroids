package nl.han.asteroids;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.scenes.*;

/**
 * Dit is het hart van je applicatie. Deze klasse erft van (extends) YaegerGame.
 * Door YaegerGame te extenden krijgt deze klasse automatisch alle engine-logica gratis mee.
 * 
 * YaegerGame Documentatie: 
 * https://han-yaeger.github.io/yaeger/hanyaeger/com/github/hanyaeger/api/YaegerGame.html
 */
public class AsteroidsGame extends YaegerGame implements PauseStateProvider {

    private int lastScore = 0;
    private boolean isPaused = false;
    private boolean audioEnabled = true;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @Override is een annotatie die aangeeft dat we een standaardmethode uit de YaegerGame engine overschrijven (vervangen).
     * setupGame() is abstract in YaegerGame, dus we zijn VERPLICHT deze methode een eigen invulling te geven.
     * Documentatie over Java Annotaties zoals @Override: 
     * https://docs.oracle.com/javase/tutorial/java/annotations/
     */
    @Override
    public void setupGame() {
        setGameTitle("Asteroids Johnny");
        setSize(new Size(GameConstants.WIDTH, GameConstants.HEIGHT));
    }

    /**
     * Ook setupScenes() MOET worden overschreven (@Override).
     * Hier registreren we alle "schermen" van de game bij de engine.
     */
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
