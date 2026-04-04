package nl.han.asteroids.entities.player;

import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.scenes.SceneBorder;
import com.github.hanyaeger.api.entities.SceneBorderCrossingWatcher;
import com.github.hanyaeger.api.userinput.KeyListener;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.KeyCode;
import nl.han.asteroids.interfaces.Hittable;
import nl.han.asteroids.interfaces.PauseStateProvider;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.ProjectileManager;
import nl.han.asteroids.managers.SoundManager;
import nl.han.asteroids.entities.player.states.PlayerState;
import nl.han.asteroids.entities.player.states.InvulnerableState;
import nl.han.asteroids.entities.player.states.NormalState;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Hey Nani! Dit is het ruimteschip van de speler.
 * 
 * OOP Principes:
 * - Overerving: Breidt DynamicSpriteEntity uit voor beweging en sprites.
 * - Interfaces: Implementeert veel interfaces voor toetsenbordinvoer (KeyListener), updates (UpdateExposer), en botsingen (Collider, Collided).
 * - State Pattern: Het schip gebruikt het State Pattern (via PlayerState) om te wisselen tussen een onkwetsbare en kwetsbare toestand na respawn.
 * 
 * Han Yaeger Engine:
 * - KeyListener: Hiermee kunnen we toetsaanslagen opvangen.
 * - UpdateExposer: Laat ons elke game-tick acties uitvoeren (via explicitUpdate).
 * - TimerContainer: Om timers toe te voegen voor schieten en onkwetsbaarheid.
 * Documentatie: https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class PlayerSpaceship extends DynamicSpriteEntity implements KeyListener, UpdateExposer, SceneBorderCrossingWatcher, Collided, Collider, TimerContainer, Hittable {
    
    private double currentFacingDirection = 0; 
    private boolean isAccelerating = false;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    
    private final ProjectileManager projectileManager;
    private final PauseStateProvider pauseStateProvider;
    private final Runnable onDeath;
    private Timer shootTimer;
    private boolean canShoot = true;
    private double savedSpeed = 0;

    private PlayerState currentState;

    /**
     * Constructor om het schip van de speler aan te maken.
     * @param initialLocation Startpositie (x, y).
     * @param projectileManager Om lasers af te kunnen vuren.
     * @param onDeath Een actie die wordt uitgevoerd als de speler sterft.
     * @param pauseStateProvider Om te controleren of het spel pauzeert.
     */
    public PlayerSpaceship(Coordinate2D initialLocation, ProjectileManager projectileManager, Runnable onDeath, PauseStateProvider pauseStateProvider) {
        super("sprites/ship_idle.png", initialLocation, new Size(96, 64));
        this.projectileManager = projectileManager;
        this.onDeath = onDeath;
        this.pauseStateProvider = pauseStateProvider;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setDirection(currentFacingDirection);
        setRotate(currentFacingDirection - 90);
        
        setState(new InvulnerableState(this));
    }

    /**
     * @Override van TimerContainer.
     * Stelt de timers in voor het schieten (cooldown) en het tijdelijk onkwetsbaar zijn bij de start.
     */
    @Override
    public void setupTimers() {
        shootTimer = new Timer(GameConstants.SHOOT_INTERVAL_PLAYER) {
            @Override
            public void onAnimationUpdate(long timestamp) {
                if (pauseStateProvider.isPaused()) return;
                canShoot = true;
                pause();
            }
        };
        shootTimer.pause();
        addTimer(shootTimer);

        addTimer(new Timer(GameConstants.INVULNERABILITY_DURATION) {
            @Override
            public void onAnimationUpdate(long timestamp) {
                if (pauseStateProvider.isPaused()) return;
                if (currentState instanceof InvulnerableState) {
                    setState(new NormalState(PlayerSpaceship.this));
                }
                remove();
            }
        });
    }

    /**
     * Verandert de toestand van het schip (onderdeel van State Pattern).
     * @param state De nieuwe toestand (bijv. NormalState).
     */
    public void setState(PlayerState state) {
        this.currentState = state;
        this.currentState.enter();
    }

    /**
     * @Override van KeyListener.
     * Vangt toetsaanslagen op en geeft deze door aan de huidige State.
     * @param pressedKeys Een set van toetsen die nu zijn ingedrukt.
     */
    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pauseStateProvider.isPaused()) {
            isRotatingLeft = false;
            isRotatingRight = false;
            isAccelerating = false;
            return;
        }
        
        if (pressedKeys.contains(KeyCode.SHIFT)) {
           // handleHyperspace(); TO-DO Later goed implementeren
        }
        
        currentState.handleInput(pressedKeys);
    }

    private void handleHyperspace() {
        var random = new Random();
        double x = random.nextDouble() * GameConstants.WIDTH;
        double y = random.nextDouble() * GameConstants.HEIGHT;
        setAnchorLocation(new Coordinate2D(x, y));
        setSpeed(0);
    }

    /**
     * Vuur een laser af als de cooldown voorbij is.
     */
    public void handleShooting() {
        if (canShoot) {
            projectileManager.spawnProjectile(getAnchorLocation(), currentFacingDirection, false);
            SoundManager.play(SoundManager.SoundType.PLAYER_FIRE);
            canShoot = false;
            shootTimer.reset();
            shootTimer.resume();
        }
    }

    /**
     * @Override van UpdateExposer.
     * Wordt elke game-tick aangeroepen en geeft de update door aan de huidige State.
     */
    @Override
    public void explicitUpdate(long timestamp) {
        if (handlePause()) return;
        currentState.update(timestamp);
    }

    private boolean handlePause() {
        if (pauseStateProvider.isPaused()) {
            if (getSpeed() > 0) {
                savedSpeed = getSpeed();
                setSpeed(0);
            }
            return true;
        }
        if (savedSpeed > 0) {
            setSpeed(savedSpeed);
            savedSpeed = 0;
        }
        return false;
    }

    /**
     * Past de natuurkunde toe op het schip: draaien, versnellen, en wrijving.
     */
    public void applyPhysics() {
        if (isRotatingLeft) currentFacingDirection += GameConstants.PLAYER_ROTATION_SPEED;
        if (isRotatingRight) currentFacingDirection -= GameConstants.PLAYER_ROTATION_SPEED;
        
        currentFacingDirection = (currentFacingDirection + 360) % 360;
        
        setDirection(currentFacingDirection);
        setRotate(currentFacingDirection - 90);
        
        if (isAccelerating) {
            setSpeed(getSpeed() + GameConstants.PLAYER_ACCELERATION);
        }
        
        if (getSpeed() > GameConstants.PLAYER_MAX_SPEED) setSpeed(GameConstants.PLAYER_MAX_SPEED);
        setSpeed(getSpeed() * GameConstants.PLAYER_FRICTION);
    }

    /**
     * @Override van SceneBorderCrossingWatcher.
     * Screen wrapping: als het schip uit het scherm vliegt, komt het aan de andere kant terug.
     */
    @Override
    public void notifyBoundaryCrossing(SceneBorder border) {
        double x = getAnchorLocation().getX();
        double y = getAnchorLocation().getY();
        if (border == SceneBorder.LEFT) x = GameConstants.WIDTH;
        else if (border == SceneBorder.RIGHT) x = 0;
        else if (border == SceneBorder.TOP) y = GameConstants.HEIGHT;
        else if (border == SceneBorder.BOTTOM) y = 0;
        setAnchorLocation(new Coordinate2D(x, y));
    }

    /**
     * @Override van Collided.
     * Geeft botsingen door aan de State.
     */
    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (pauseStateProvider.isPaused()) return;
        currentState.onCollision(collidingObjects);
    }

    /**
     * @Override van Hittable.
     * Geeft aan dat we door een specifiek object zijn geraakt, afgehandeld in de State.
     */
    @Override
    public void onHitBy(Collider collider) {
        if (pauseStateProvider.isPaused()) return;
        currentState.onHitBy(collider);
    }

    /**
     * Vernietigt het schip en roept het onDeath event aan.
     */
    public void destroy() {
        remove();
        if (onDeath != null) {
            onDeath.run();
        }
    }

    public void setRotatingLeft(boolean val) { this.isRotatingLeft = val; }
    public void setRotatingRight(boolean val) { this.isRotatingRight = val; }
    public void setAccelerating(boolean val) { this.isAccelerating = val; }
}