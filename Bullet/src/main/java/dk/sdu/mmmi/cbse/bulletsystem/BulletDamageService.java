package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageSourceService;

public class BulletDamageService implements IDamageSourceService {

    @Override
    public boolean handles(Entity entity) {
        return entity instanceof Bullet;
    }

    @Override
    public int getDamage(Entity entity) {
        return 20;
    }
}
