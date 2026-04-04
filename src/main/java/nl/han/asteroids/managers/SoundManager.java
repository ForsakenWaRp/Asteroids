package nl.han.asteroids.managers;

import com.github.hanyaeger.api.media.SoundClip;
import java.util.HashMap;
import java.util.Map;

/**
 * Hey Nani, dit is de SoundManager!
 * Deze klasse gebruikt een variant van het **Singleton Pattern** (via static variabelen en methodes) 
 * zodat elk onderdeel van de game makkelijk geluiden kan afspelen zonder steeds nieuwe managers aan te maken.
 * Ook hier zien we het **Single Responsibility Principle** terug: alleen audiozaken gebeuren hier.
 * 
 * We gebruiken hier de SoundClip klasse van Han Yaeger.
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class SoundManager {

    /**
     * Een Enum om de verschillende geluidstypes aan te duiden. Handig om type-fouten te voorkomen!
     */
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
            sounds.put(type, clip);
        }
    }

    /**
     * Schakelt alle audio in of uit.
     * 
     * @param enabled True om audio aan te zetten, false om te dempen.
     */
    public static void setAudioEnabled(boolean enabled) {
        audioEnabled = enabled;
        if (!enabled) stopAll();
    }

    /**
     * Speelt een specifiek geluid af.
     * 
     * @param type Het type geluid (uit onze SoundType enum) dat afgespeeld moet worden.
     */
    public static void play(SoundType type) {
        if (audioEnabled && sounds.containsKey(type)) {
            sounds.get(type).play();
        }
    }

    /**
     * Stopt een specifiek geluid.
     * 
     * @param type Het geluid dat gestopt moet worden.
     */
    public static void stop(SoundType type) {
        if (sounds.containsKey(type)) {
            sounds.get(type).stop();
        }
    }

    /**
     * Stopt alle geluiden die op dit moment spelen.
     */
    public static void stopAll() {
        for (SoundClip clip : sounds.values()) {
            clip.stop();
        }
    }

    private SoundManager() {}
}
