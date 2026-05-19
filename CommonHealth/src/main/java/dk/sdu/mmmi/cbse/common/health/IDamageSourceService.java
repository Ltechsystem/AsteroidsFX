package dk.sdu.mmmi.cbse.common.health;

import dk.sdu.mmmi.cbse.common.data.Entity;

public interface IDamageSourceService {
    boolean handles(Entity entity);
    int getDamage(Entity entity);
}
