package com.samyyc.invader.gun.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class GameData {

    public static final int ASSAULT_RIFLE_AMMO = 60;
    public static final double ASSAULT_RIFLE_COOLDOWN = 0.5;
    public static final int ASSAULT_RIFLE_MAX_DISTANCE = 60;
    public static final int ASSAULT_RIFLE_DAMAGE = 2;
    public static final ItemStack ASSAULT_RIFLE_ITEMSTACK = ItemStack
            .builder(Material.IRON_HOE)
            .displayName(Component.text("Ak-47突击步枪", NamedTextColor.RED))
            .build();

    public static final int RPG_AMMO = 5;
    public static final double RPG_COOLDOWN = 10;
    public static final int RPG_MAX_DISTANCE = 30;
    public static final int RPG_DAMAGE = 100;
    public static final ItemStack RPG_ITEMSTACK = ItemStack
            .builder(Material.DIAMOND_SHOVEL)
            .displayName(Component.text("RPG", NamedTextColor.AQUA))
            .build();


}
