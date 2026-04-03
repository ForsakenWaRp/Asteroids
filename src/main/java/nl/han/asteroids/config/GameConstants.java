package nl.han.asteroids.config;

import javafx.scene.paint.Color;

public class GameConstants {
    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;
    public static final Color BACKGROUND_COLOR = Color.BLACK;

    public static final int SCENE_MAIN_MENU = 0;
    public static final int SCENE_GAME = 1;
    public static final int SCENE_HIGHSCORES = 2;
    public static final int SCENE_GAME_OVER = 3;

    public static final int INITIAL_LIVES = 3;
    public static final long SHOOT_INTERVAL_PLAYER = 250;
    public static final long SHOOT_INTERVAL_UFO = 2000;
    
    public static final long INVULNERABILITY_DURATION = 1500;
    public static final double PLAYER_ROTATION_SPEED = 4.5;
    public static final double PLAYER_ACCELERATION = 0.15;
    public static final double PLAYER_MAX_SPEED = 7.0;
    public static final double PLAYER_FRICTION = 0.992;
}
