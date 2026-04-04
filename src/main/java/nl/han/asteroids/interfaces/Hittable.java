package nl.han.asteroids.interfaces;

import com.github.hanyaeger.api.entities.Collider;

/**
 * Hey Nani! Dit is de Hittable interface.
 * Dit volgt het **Tell, Don't Ask principe**. In plaats van te controleren wat voor type object we raken
 * en dan de logica daar te schrijven, vertellen we het object gewoon: "Je bent geraakt door dit object, doe je ding!".
 * Dit is de kracht van polymorfisme in OOP.
 * 
 * Han Yaeger API Info:
 * https://han-yaeger.github.io/yaeger/hanyaeger/module-summary.html
 */
public interface Hittable {
    /**
     * Wordt aangeroepen wanneer dit object ergens mee botst.
     * 
     * @param collider Het object waarmee we in botsing zijn gekomen.
     */
    void onHitBy(Collider collider);
}