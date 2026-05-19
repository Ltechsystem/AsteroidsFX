package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.health.IDamageable;
import dk.sdu.mmmi.cbse.common.health.IDamageSource;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

public class CollisionDetector implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) continue;
                if (!world.getEntities().contains(entity1) || !world.getEntities().contains(entity2)) continue;

                String group1 = entity1.getCollisionGroup();
                String group2 = entity2.getCollisionGroup();
                if (group1 != null && group1.equals(group2)) continue;
                if (group2 != null && entity1.getIgnoredCollisionGroups().contains(group2)) continue;
                if (group1 != null && entity2.getIgnoredCollisionGroups().contains(group1)) continue;

                if (!collides(entity1, entity2)) continue;

                if (entity1 instanceof IDamageable && entity2 instanceof IDamageSource) {
                    applyDamage((IDamageable) entity1, entity1, (IDamageSource) entity2, entity2, world);
                } else if (entity2 instanceof IDamageable && entity1 instanceof IDamageSource) {
                    applyDamage((IDamageable) entity2, entity2, (IDamageSource) entity1, entity1, world);
                } else if (entity1 instanceof IDamageable) {
                    world.removeEntity(entity2);
                } else if (entity2 instanceof IDamageable) {
                    world.removeEntity(entity1);
                } else {
                    world.removeEntity(entity1);
                    world.removeEntity(entity2);
                }
            }
        }
    }

    private void applyDamage(IDamageable damageable, Entity damageableEntity,
                              IDamageSource source, Entity sourceEntity, World world) {
        damageable.setHealth(damageable.getHealth() - source.getDamage());
        world.removeEntity(sourceEntity);
        if (damageable.getHealth() <= 0) {
            world.removeEntity(damageableEntity);
        }
    }

    private boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}
