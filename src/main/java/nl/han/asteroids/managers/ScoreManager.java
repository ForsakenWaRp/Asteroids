package nl.han.asteroids.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.han.asteroids.models.PlayerScore;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Beheert het laden en opslaan van highscores en de laatst gebruikte speler naam
 * via de Java Preferences API, zodat er geen losse bestanden nodig zijn.
 */
public class ScoreManager {
    private static final String PREF_SCORES = "highscores";
    private static final String PREF_LAST_NAME = "last_name";
    private static final Preferences prefs = Preferences.userNodeForPackage(ScoreManager.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<PlayerScore> loadScores() {
        String json = prefs.get(PREF_SCORES, "[]");
        try {
            return mapper.readValue(json, new TypeReference<List<PlayerScore>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

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

    public static String getLastUsedName() {
        return prefs.get(PREF_LAST_NAME, "Player");
    }
}