import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IWaveSpawnerService;

module Asteroid {
    requires Common;
    requires CommonAsteroids;
    requires CommonHealth;
    provides IGamePluginService with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.asteroid.AsteroidProcessor;
    provides IWaveSpawnerService with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.mmmi.cbse.common.health.IDamageSourceService with dk.sdu.mmmi.cbse.asteroid.AsteroidDamageService;
}