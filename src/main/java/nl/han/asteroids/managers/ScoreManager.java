package nl.han.asteroids.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.han.asteroids.models.PlayerScore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Beheert het laden en opslaan van highscores en de laatst gebruikte speler naam.
 */
public class ScoreManager {
    private static final String FILE_PATH = "highscores.json";
    private static final String NAME_PATH = "lastname.txt";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<PlayerScore> loadScores() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(file, new TypeReference<List<PlayerScore>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void saveScore(PlayerScore newScore) {
        List<PlayerScore> scores = loadScores();
        scores.add(newScore);
        scores.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        try {
            mapper.writeValue(new File(FILE_PATH), scores);

            Files.writeString(new File(NAME_PATH).toPath(), newScore.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLastUsedName() {
        File file = new File(NAME_PATH);
        if (!file.exists()) return "Player";
        try {
            return Files.readString(file.toPath()).trim();
        } catch (IOException e) {
            return "Player";
        }
    }
}
