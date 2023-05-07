package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Represents a player animation event
 */
public class PlayerHealthDecreaseEvent extends PlayerEvent {

    private int calculatedDamage = 0;

    /**
     * Construct a new PlayerHealthDecreaseEvent event
     *
     * @param player The player instance
     * @param calculatedDamage The damage, after the armor calculations have been made
     */
    public PlayerHealthDecreaseEvent(final Player player, final int calculatedDamage) {
        super(Type.PLAYER_HEALTH_DECREASE, player);
        this.calculatedDamage = calculatedDamage;
    }

    /**
     * Get the type of this animation event
     *
     * @return the animation type
     */
    public int getCalculatedDamage() {
        return calculatedDamage;
    }

    public void setCalculatedDamage(int damage) {
        this.calculatedDamage = damage;
    }
}