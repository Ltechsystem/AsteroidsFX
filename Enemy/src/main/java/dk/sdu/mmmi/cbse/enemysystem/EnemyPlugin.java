package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }

    private Entity createEnemy(GameData gameData) {
        Entity e = new Enemy();
        e.setPolygonCoordinates(0, -12, 12, 0, 0, 12, -12, 0);
        e.setRadius(12);

        Random rnd = new Random();
        int edge = rnd.nextInt(4);
        if (edge == 0) {
            e.setX(rnd.nextInt(gameData.getDisplayWidth()));
            e.setY(0);
        } else if (edge == 1) {
            e.setX(rnd.nextInt(gameData.getDisplayWidth()));
            e.setY(gameData.getDisplayHeight());
        } else if (edge == 2) {
            e.setX(0);
            e.setY(rnd.nextInt(gameData.getDisplayHeight()));
        } else {
            e.setX(gameData.getDisplayWidth());
            e.setY(rnd.nextInt(gameData.getDisplayHeight()));
        }
        e.setRotation(rnd.nextInt(360));
        e.setCollisionGroup("enemy");
        e.setFillColor("#CE2029");
        return e;
    }
}
