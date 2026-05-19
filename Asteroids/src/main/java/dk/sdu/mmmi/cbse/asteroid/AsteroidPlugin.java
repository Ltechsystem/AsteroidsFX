package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IWaveSpawnerService;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService, IWaveSpawnerService {

    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    @Override
    public void spawnWave(int waveNumber, GameData gameData, World world) {
        Random rnd = new Random();
        int count = waveNumber + 1 + rnd.nextInt(waveNumber + 1);
        for (int i = 0; i < count; i++) {
            world.addEntity(createAsteroid(gameData));
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        Random rnd = new Random();
        int size = rnd.nextInt(10) + 10;
        int sides = 7;
        double[] coords = new double[sides * 2];
        for (int i = 0; i < sides; i++) {
            double angle = Math.toRadians(i * (360.0 / sides) - 90);
            coords[i * 2]     = size * Math.cos(angle);
            coords[i * 2 + 1] = size * Math.sin(angle);
        }
        asteroid.setPolygonCoordinates(coords);
        asteroid.setX(rnd.nextInt(gameData.getDisplayWidth()));
        asteroid.setY(rnd.nextInt(gameData.getDisplayHeight()));
        asteroid.setRadius(size);
        asteroid.setRotation(rnd.nextInt(360));
        asteroid.setCollisionGroup("asteroid");
        asteroid.setFillColor("#1C1C1C");
        return asteroid;
    }
}
