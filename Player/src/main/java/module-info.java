
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Player {
    requires Common;
    requires CommonBullet;
    requires CommonHealth;   
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.playersystem.PlayerPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
    provides dk.sdu.mmmi.cbse.common.health.IDamageableService with dk.sdu.mmmi.cbse.playersystem.PlayerHealthService;
}
