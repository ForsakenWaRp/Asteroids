package nl.han.asteroids.entities.player;

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
import nl.han.asteroids.AsteroidsGame;
import nl.han.asteroids.config.GameConstants;
import nl.han.asteroids.managers.ProjectileManager;
import nl.han.asteroids.managers.SoundManager;
import nl.han.asteroids.entities.player.states.PlayerState;
import nl.han.asteroids.entities.player.states.InvulnerableState;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Vertegenwoordigt het ruimteschip van de speler, gestuurd door een State Pattern.
 */
public class PlayerSpaceship extends DynamicSpriteEntity implements KeyListener, UpdateExposer, SceneBorderCrossingWatcher, Collided, Collider {
    

    private double currentFacingDirection = 0; 
    private boolean isAccelerating = false;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    
    private final ProjectileManager projectileManager;
    private final AsteroidsGame asteroidsGame;
    private final Runnable onDeath;
    private long lastShotTime = 0;
    private double savedSpeed = 0;

    private PlayerState currentState;

    public PlayerSpaceship(Coordinate2D initialLocation, ProjectileManager projectileManager, Runnable onDeath, AsteroidsGame asteroidsGame) {
        super("sprites/ship_idle.png", initialLocation, new Size(96, 64));
        this.projectileManager = projectileManager;
        this.onDeath = onDeath;
        this.asteroidsGame = asteroidsGame;
        
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setDirection(currentFacingDirection);
        setRotate(currentFacingDirection - 90);
        
        setState(new InvulnerableState(this));
    }

    public void setState(PlayerState state) {
        this.currentState = state;
        this.currentState.enter();
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (asteroidsGame.isPaused()) {
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

    public void handleShooting() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime > GameConstants.SHOOT_INTERVAL_PLAYER) {
            projectileManager.spawnProjectile(getAnchorLocation(), currentFacingDirection, false);
            SoundManager.play(SoundManager.SoundType.PLAYER_FIRE);
            lastShotTime = currentTime;
        }
    }

    @Override
    public void explicitUpdate(long timestamp) {
        if (handlePause()) return;
        currentState.update(timestamp);
    }

    private boolean handlePause() {
        if (asteroidsGame.isPaused()) {
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

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (asteroidsGame.isPaused()) return;
        currentState.onCollision(collidingObjects);
    }

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
