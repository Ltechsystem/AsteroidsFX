module CommonHealth {
    requires Common;
    exports dk.sdu.mmmi.cbse.common.health;
    uses dk.sdu.mmmi.cbse.common.health.IDamageableService;
    uses dk.sdu.mmmi.cbse.common.health.IDamageSourceService;
}
