import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IWaveSpawnerService;

module Enemy {
    requires Common;
    requires CommonBullet;
    requires CommonAsteroids;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;
    provides IWaveSpawnerService with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
}
