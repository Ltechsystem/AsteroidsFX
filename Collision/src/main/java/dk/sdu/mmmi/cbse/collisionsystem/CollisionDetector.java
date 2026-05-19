package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.health.IDamageableService;
import dk.sdu.mmmi.cbse.common.health.IDamageSourceService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class CollisionDetector implements IPostEntityProcessingService {

    private final List<IDamageableService> damageableServices =
            ServiceLoader.load(IDamageableService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    private final List<IDamageSourceService> damageSourceServices =
            ServiceLoader.load(IDamageSourceService.class).stream().map(ServiceLoader.Provider::get).collect(toList());

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

                IDamageableService ds1 = findDamageable(entity1);
                IDamageSourceService src2 = findDamageSource(entity2);
                IDamageableService ds2 = findDamageable(entity2);
                IDamageSourceService src1 = findDamageSource(entity1);

                if (ds1 != null && src2 != null) {
                    ds1.applyDamage(entity1, src2.getDamage(entity2), world);
                    world.removeEntity(entity2);
                } else if (ds2 != null && src1 != null) {
                    ds2.applyDamage(entity2, src1.getDamage(entity1), world);
                    world.removeEntity(entity1);
                } else {
                    world.removeEntity(entity1);
                    world.removeEntity(entity2);
                }
            }
        }
    }

    private IDamageableService findDamageable(Entity entity) {
        return damageableServices.stream().filter(s -> s.handles(entity)).findFirst().orElse(null);
    }

    private IDamageSourceService findDamageSource(Entity entity) {
        return damageSourceServices.stream().filter(s -> s.handles(entity)).findFirst().orElse(null);
    }

    private boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}
