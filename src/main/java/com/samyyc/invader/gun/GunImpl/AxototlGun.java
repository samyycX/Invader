package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.bullet.Bullet;
import com.samyyc.invader.gun.bullet.BulletManager;
import com.samyyc.invader.gun.packet.GamePacket;
import com.samyyc.invader.util.Pair;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class AxototlGun implements Gun {
    @Override
    public void fire(@NotNull Player player) {

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
                    return Pair.of(pos, false);
                }
        );

        BulletManager.submitBullet(bullet, 999);

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
