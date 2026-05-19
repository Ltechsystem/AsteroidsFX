package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSource;

public class Asteroid extends Entity implements IDamageSource {

    @Override
    public int getDamage() { return 50; }
}
