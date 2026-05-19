package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {

    private final Random random = new Random();
    private long lastDirectionChange = 0;
    private long lastShotTime = 0;

    private final long directionChangeInterval = new Random().nextInt(1500) + 1500;
    private static final long bulletFireRate = 2000;
    private static final double bulletSpeed = 1.5;
    private final double enemySpeed = new Random().nextDouble(0.75) + 0.75;
    private static final double aimVariance = 5;

    @Override
    public void process(GameData gameData, World world) {
        long now = System.currentTimeMillis();

        for (Entity enemy : world.getEntities(Enemy.class)) {
            if (now - lastDirectionChange >= directionChangeInterval) {
                enemy.setRotation(random.nextInt(360));
                lastDirectionChange = now;
            }

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
            enemy.setX(enemy.getX() + changeX * enemySpeed);
            enemy.setY(enemy.getY() + changeY * enemySpeed);

            if (enemy.getX() < 0) enemy.setX(gameData.getDisplayWidth());
            if (enemy.getX() > gameData.getDisplayWidth()) enemy.setX(0);
            if (enemy.getY() < 0) enemy.setY(gameData.getDisplayHeight());
            if (enemy.getY() > gameData.getDisplayHeight()) enemy.setY(0);

            if (now - lastShotTime >= bulletFireRate) {
                Entity target = findPlayer(enemy, world);
                if (target != null) {
                    double dx = target.getX() - enemy.getX();
                    double dy = target.getY() - enemy.getY();
                    final double aimAngle = Math.toDegrees(Math.atan2(dy, dx))
                            + (random.nextDouble() * 2 * aimVariance) - aimVariance;

                    getBulletSPIs().stream().findFirst().ifPresent(spi -> {
                        double prev = enemy.getRotation();
                        enemy.setRotation(aimAngle);
                        Entity bullet = spi.createBullet(enemy, gameData);
                        bullet.setSpeed(bulletSpeed);
                        world.addEntity(bullet);
                        enemy.setRotation(prev);
                    });
                    lastShotTime = now;
                }
            }
        }
    }

    private Entity findPlayer(Entity enemy, World world) {
        Entity nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Entity e : world.getEntities()) {
            if (e instanceof Enemy || e instanceof Bullet || e instanceof Asteroid) continue;
            double dx = e.getX() - enemy.getX();
            double dy = e.getY() - enemy.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDist) {
                minDist = dist;
                nearest = e;
            }
        }
        return nearest;
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
