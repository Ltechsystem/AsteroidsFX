package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSource;

public class Bullet extends Entity implements IDamageSource {

    @Override
    public int getDamage() { return 20; }
}
