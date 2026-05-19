package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSourceService;

public class EnemyDamageService implements IDamageSourceService {

    @Override
    public boolean handles(Entity entity) {
        return entity instanceof Enemy;
    }

    @Override
    public int getDamage(Entity entity) {
        return 30;
    }
}
