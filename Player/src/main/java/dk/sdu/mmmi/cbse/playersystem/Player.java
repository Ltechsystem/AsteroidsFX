package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.health.IDamageable;

public class Player extends Entity implements IDamageable {

    private int health = 100;
    private final int maxHealth = 100;

    @Override
    public int getHealth() { return health; }

    @Override
    public void setHealth(int health) { this.health = health; }

    @Override
    public int getMaxHealth() { return maxHealth; }
}
