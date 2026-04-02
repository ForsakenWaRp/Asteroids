package nl.han.asteroids.managers;

import com.github.hanyaeger.api.media.SoundClip;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    public enum SoundType {
        MENU_THEME("audio/mainMenu.wav"),
        PLAYER_FIRE("audio/fire.wav"),
        ASTEROID_EXPLOSION("audio/astroidexplosion.wav"),
        UFO_FIRE("audio/ufoShoot.wav"),
        UFO_ENGINE("audio/asteroid-ufo.mp3");

        private final String path;
        SoundType(String path) { this.path = path; }
        public String getPath() { return path; }
    }

    private static final Map<SoundType, SoundClip> sounds = new HashMap<>();
    private static boolean audioEnabled = true;

    static {
        for (SoundType type : SoundType.values()) {
            var clip = new SoundClip(type.getPath());
            if (type == SoundType.MENU_THEME || type == SoundType.UFO_ENGINE) {
            }
            sounds.put(type, clip);
        }
    }

    public static void setAudioEnabled(boolean enabled) {
        audioEnabled = enabled;
        if (!enabled) stopAll();
    }

    public static void play(SoundType type) {
        if (audioEnabled && sounds.containsKey(type)) {
            sounds.get(type).play();
        }
    }

    public static void stop(SoundType type) {
        if (sounds.containsKey(type)) {
            sounds.get(type).stop();
        }
    }

    public static void stopAll() {
        for (SoundClip clip : sounds.values()) {
            clip.stop();
        }
    }

    private SoundManager() {}
}
