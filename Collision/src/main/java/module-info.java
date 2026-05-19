import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires CommonHealth;
    uses dk.sdu.mmmi.cbse.common.health.IDamageableService;
    uses dk.sdu.mmmi.cbse.common.health.IDamageSourceService;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}