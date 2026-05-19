package dk.sdu.mmmi.cbse.common.health;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IDamageableService {
    boolean handles(Entity entity);
    void applyDamage(Entity entity, int damage, World world);
    int getHealth(Entity entity);
    int getMaxHealth(Entity entity);
}
