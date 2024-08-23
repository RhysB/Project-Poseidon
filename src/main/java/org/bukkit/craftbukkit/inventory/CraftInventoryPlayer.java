package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.InventoryPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class CraftInventoryPlayer extends CraftInventory implements PlayerInventory {
    public CraftInventoryPlayer(net.minecraft.server.InventoryPlayer inventory) {
        super(inventory);
    }

    public InventoryPlayer getInventory() {
        return (InventoryPlayer) inventory;
    }

    public int getSize() {
        return super.getSize() - 4;
    }

    public ItemStack getItemInHand() {
        return new CraftItemStack(getInventory().getItemInHand());
    }

    public void setItemInHand(ItemStack stack) {
        setItem(getHeldItemSlot(), stack);
    }

    public int getHeldItemSlot() {
        return getInventory().itemInHandIndex;
    }

    public ItemStack getHelmet() {
        return getItem(getSize() + 3);
    }

    public ItemStack getChestplate() {
        return getItem(getSize() + 2);
    }

    public ItemStack getLeggings() {
        return getItem(getSize() + 1);
    }

    public ItemStack getBoots() {
        return getItem(getSize());
    }

    public void setHelmet(ItemStack helmet) {
        setItem(getSize() + 3, helmet);
    }

    public void setChestplate(ItemStack chestplate) {
        setItem(getSize() + 2, chestplate);
    }

    public void setLeggings(ItemStack leggings) {
        setItem(getSize() + 1, leggings);
    }

    public void setBoots(ItemStack boots) {
        setItem(getSize(), boots);
    }

    public ItemStack[] getArmorContents() {
        ArrayList<ItemStack> contents = new ArrayList<>();

        ItemStack boots = getItem(getSize());
        if (boots != null && boots.getType() != Material.AIR) {
            contents.add(boots);
        }

        ItemStack leggings = getItem(getSize() + 1);
        if (leggings != null && leggings.getType() != Material.AIR) {
            contents.add(leggings);
        }

        ItemStack chestplate = getItem(getSize() + 2);
        if (chestplate != null && chestplate.getType() != Material.AIR) {
            contents.add(chestplate);
        }

        ItemStack helmet = getItem(getSize() + 3);
        if (helmet != null && helmet.getType() != Material.AIR) {
            contents.add(helmet);
        }

        return contents.toArray(new ItemStack[0]);
    }

    public void setArmorContents(ItemStack[] items) {
        int cnt = getSize();

        if (items == null) {
            items = new ItemStack[4];
        }
        for (ItemStack item : items) {
            if (item == null || item.getTypeId() == 0) {
                clear(cnt++);
            } else {
                setItem(cnt++, item);
            }
        }
    }
}
