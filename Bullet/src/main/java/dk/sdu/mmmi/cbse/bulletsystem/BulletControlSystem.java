package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {
            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));
            double speed = bullet.getSpeed() > 0 ? bullet.getSpeed() : 3;
            bullet.setX(bullet.getX() + changeX * speed);
            bullet.setY(bullet.getY() + changeY * speed);

            if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                    || bullet.getY() < gameData.getDisplayTopBound() || bullet.getY() > gameData.getDisplayHeight()) {
                world.removeEntity(bullet);
            } // Removes bullets after leaing screen bounds
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        double changeX = Math.cos(Math.toRadians(shooter.getRotation()));
        double changeY = Math.sin(Math.toRadians(shooter.getRotation()));
        double spawnOffset = shooter.getRadius() * 2 + 5;
        bullet.setX(shooter.getX() + changeX * spawnOffset);
        bullet.setY(shooter.getY() + changeY * spawnOffset);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);
        return bullet;
    }
}
