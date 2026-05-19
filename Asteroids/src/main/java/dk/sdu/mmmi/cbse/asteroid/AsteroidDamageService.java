package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSourceService;

public class AsteroidDamageService implements IDamageSourceService {

    @Override
    public boolean handles(Entity entity) {
        return entity instanceof Asteroid;
    }

    @Override
    public int getDamage(Entity entity) {
        return 50;
    }
}
