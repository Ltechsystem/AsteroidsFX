package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSource;

public class Enemy extends Entity implements IDamageSource {

    @Override
    public int getDamage() { return 30; }
}
