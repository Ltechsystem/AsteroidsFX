package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.health.IDamageableService;

public class PlayerHealthService implements IDamageableService {

    @Override
    public boolean handles(Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public void applyDamage(Entity entity, int damage, World world) {
        Player player = (Player) entity;
        player.setHealth(player.getHealth() - damage);
        if (player.getHealth() <= 0) {
            world.removeEntity(entity);
        }
    }

    @Override
    public int getHealth(Entity entity) {
        return ((Player) entity).getHealth();
    }

    @Override
    public int getMaxHealth(Entity entity) {
        return ((Player) entity).getMaxHealth();
    }
}
