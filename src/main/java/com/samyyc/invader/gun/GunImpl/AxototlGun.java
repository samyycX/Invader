package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.bullet.Bullet;
import com.samyyc.invader.gun.bullet.BulletManager;
import com.samyyc.invader.gun.packet.GamePacket;
import com.samyyc.invader.util.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.biomes.BiomeEffects;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class AxototlGun implements Gun {
    @Override
    public void fire(@NotNull Player player, BulletManager bulletManager) {

        Bullet bullet = new Bullet();
        bullet.setDamage(1);
        bullet.setCanBreakBlock(false);
        bullet.setPlayer(player);
        bullet.setInterval(1);
        bullet.setEnded(false);
        bullet.setPos(player.getPosition());

        Entity entity = new Entity(EntityType.AXOLOTL);
        entity.setInstance(player.getInstance(), player.getPosition());
        entity.lookAt(player.getPosition().direction().normalize().mul(2).asPosition());
        entity.setVelocity(player.getPosition().direction().mul(30));
        entity.setGravity(0, 0);

        bullet.setPredicate(
                (pos) -> {
                    GamePacket.sendPacket(player, getFiringParticle(entity.getPosition()));
                    return entity.getPosition();
                }
        );

        bullet.setOnEntityPredicate((pos, entity1, bullet1) -> {
            entity1.getInstance().setBlock(pos, Block.ICE);
            entity1.getInstance().setBlock(pos.add(0,1,0), Block.ICE);
            if (entity1 instanceof Player player1) {
                player1.playSound(Sound.sound(Key.key("block.glass.break"), Sound.Source.AMBIENT, 0.5F, 0.5F));
            }
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                entity1.getInstance().setBlock(entity1.getPosition(), Block.AIR);
                entity1.getInstance().setBlock(entity1.getPosition().add(0,1,0), Block.AIR);

            }).delay(Duration.of(5, TimeUnit.SECOND));
            
            return true;
        });

        bulletManager.submitBullet(bullet, 999);

    }

    @Override
    public int getAmmo() {
        return 100;
    }

    @Override
    public ItemStack getRawItemStack() {
        return ItemStack.builder(Material.DIAMOND).build();
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public double getCoolDown() {
        return 2;
    }

    @Override
    public int getMaxFlyingDistance() {
        return 100;
    }

    @Override
    public int getEachAmmoReloadTime() {
        return 100;
    }
}
