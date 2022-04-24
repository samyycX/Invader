package com.samyyc.invader.gun;

import com.samyyc.invader.game.PlayerKillPlayerEvent;
import com.samyyc.invader.gun.bullet.BulletManager;
import com.samyyc.invader.gun.data.GameData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.network.packet.server.play.SetCooldownPacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public interface Gun {

    public static double VECTOR_TWO_POINT_INTERVAL = 0.6;
    public static double HIT_RANGE_CHECK = 2;
    public static int TICK_TO_MS = 1000 / MinecraftServer.TICK_PER_SECOND;

    void fire(@NotNull Player player, BulletManager bulletManager);

    int getAmmo();

    ItemStack getRawItemStack();

    default ItemStack getItemStack() {
        return getRawItemStack()
                .withTag(Tag.String("gun"), getTag())
                .withTag(Tag.Integer("ammo"), getAmmo())
                .withTag(Tag.Boolean("reloading"), false)
                .withTag(Tag.Long("lastRefresh"), (long) 0);
    }

    int getDamage();

    double getCoolDown();

    int getMaxFlyingDistance();

    default Sound getHitSound() {
        Sound hitSound = Sound.sound(
                Key.key("entity.arrow.hit_player"),
                Sound.Source.AMBIENT,
                10,
                0
        );
        return hitSound;
    };

    default Sound getFiringSound() {
        Sound firingSound = Sound.sound(
                Key.key("block.piston.contract"),
                Sound.Source.AMBIENT,
                20,
                2
        );
        return firingSound;
    };

    default ParticlePacket getFiringParticle(Pos pos) {
        ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                Particle.GLOW,
                false,
                pos.x(),
                pos.y(),
                pos.z(),
                0F,
                0F,
                0F,
                Block.GRASS_BLOCK.stateId(),
                0,
                null
        );
        return particlePacket;
    }

    default String getTag() {
        return this.getClass()
                .getSimpleName()
                .toLowerCase();
    };

    default boolean hasAmmo(Player player) {
        ItemStack gun = player.getItemInMainHand();
        if (!gun.isAir() && gun.hasTag(Tag.Integer("ammo"))) {
            return gun.getTag(Tag.Integer("ammo")) != 0;
        } else return false;
    }

    default void reloadAmmo(Player player, int slot) {
        if (player.getInventory().getItemStack(slot).hasTag(Tag.Integer("ammo"))) {
            AtomicInteger nowAmmo = new AtomicInteger(player.getInventory().getItemStack(slot).getTag(Tag.Integer("ammo")));
            if (nowAmmo.get() >= getAmmo()) {
                return;
            }

            MinecraftServer.getSchedulerManager().submitTask(() -> {

                String ammoHad = "|".repeat(nowAmmo.get());

                String ammoNeedToReload = "|".repeat(getAmmo() - nowAmmo.get());

                player.sendActionBar(Component.text(ammoHad, NamedTextColor.GREEN)
                        .append(Component.text(ammoNeedToReload, NamedTextColor.GRAY)));

                ItemStack itemStack = player.getInventory().getItemStack(slot);
                //System.out.println(itemStack);
                if (nowAmmo.get() != getAmmo()) {
                    nowAmmo.getAndIncrement();
                } else {
                    player.getInventory().setItemStack(slot,
                            itemStack
                                    .withTag(Tag.Integer("ammo"), getAmmo())
                                    .withTag(Tag.Boolean("reloading"),false)
                    );      
                    return TaskSchedule.stop();
                }

                player.getInventory().setItemStack(slot, itemStack.withTag(Tag.Boolean("reloading"), true));
                return TaskSchedule.tick(getEachAmmoReloadTime());

            }, ExecutionType.SYNC);
        }
    }

    int getEachAmmoReloadTime();

    default void sendCooldownPacket(Player player) {
        player.sendPacket(
                new SetCooldownPacket(
                        player.getItemInMainHand().material().id(),
                        (int) Math.floor(getCoolDown() / 2)
                )
        );
    }



}
