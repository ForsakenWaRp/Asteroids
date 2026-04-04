package nl.han.asteroids.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.han.asteroids.models.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Hey Nani, de ScoreManager regelt de 'Highscores'!
 * Deze manager volgt het **Single Responsibility Principle** door zich puur te richten op het opslaan
 * en laden van speler scores, en niets anders.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public class ScoreManager {
    private static final String PREF_SCORES = "highscores";
    private static final String PREF_LAST_NAME = "last_name";
    private static final Preferences prefs = Preferences.userNodeForPackage(ScoreManager.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Laadt de lijst met opgeslagen highscores.
     * 
     * @return Een lijst met PlayerScore objecten.
     */
    public static List<PlayerScore> loadScores() {
        String json = prefs.get(PREF_SCORES, "[]");
        try {
            return mapper.readValue(json, new TypeReference<List<PlayerScore>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Slaat een nieuwe score op en houdt alleen de top 10 bij.
     * 
     * @param newScore De nieuwe score van de speler die we willen opslaan.
     */
    public static void saveScore(PlayerScore newScore) {
        List<PlayerScore> scores = loadScores();
        scores.add(newScore);
        scores.sort((s1, s2) -> Integer.compare(s2.score(), s1.score()));
        
        // Keep only top 10 scores
        if (scores.size() > 10) {
            scores = new ArrayList<>(scores.subList(0, 10));
        }

        try {
            String json = mapper.writeValueAsString(scores);
            prefs.put(PREF_SCORES, json);
            prefs.put(PREF_LAST_NAME, newScore.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Haalt de laatst gebruikte spelernaam op, handig om direct in te vullen!
     * 
     * @return De laatst gebruikte naam, of "Player" als er nog geen is.
     */
    public static String getLastUsedName() {
        return prefs.get(PREF_LAST_NAME, "Player");
    }
}