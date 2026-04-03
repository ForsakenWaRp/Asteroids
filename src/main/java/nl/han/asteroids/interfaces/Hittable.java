package nl.han.asteroids.interfaces;

import com.github.hanyaeger.api.entities.Collider;

/**
 * Interface voor objecten die geraakt kunnen worden (Tell, Don't Ask principe).
 */
public interface Hittable {
    void onHitBy(Collider collider);
}