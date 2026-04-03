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
    
    public static final double UFO_SPAWN_CHANCE = 0.20;
    public static final int UFO_SCORE = 100;
    public static final int ASTEROID_BASE_SCORE = 10;
    
    public static final long COMBO_TIMEOUT = 2000;
    public static final long MIN_SPAWN_INTERVAL = 800;
    public static final long INITIAL_SPAWN_INTERVAL = 3000;
    public static final int SPAWN_INTERVAL_SCORE_DIVISOR = 10;
    public static final int ASTEROID_MAX_SIZE = 3;
    public static final double UFO_SPEED = 4.0;
    public static final int BACKGROUND_STARS_PER_LAYER = 50;
    
    public static final int PARTICLES_PER_ASTEROID_EXPLOSION = 10;
    public static final int PARTICLES_PER_UFO_EXPLOSION = 20;
    public static final double LASER_SPEED = 8.0;
}
