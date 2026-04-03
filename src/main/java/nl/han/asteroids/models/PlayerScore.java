package nl.han.asteroids.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record voor een speler score.
 */
public record PlayerScore(String name, int score) {
    
    @JsonCreator
    public PlayerScore(
            @JsonProperty("name") String name, 
            @JsonProperty("score") int score) {
        this.name = name;
        this.score = score;
    }
}