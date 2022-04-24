package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.bullet.Bullet;
import com.samyyc.invader.gun.bullet.BulletManager;
import com.samyyc.invader.gun.packet.GamePacket;
import com.samyyc.invader.util.Pair;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.registry.Registry;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

public class AWP implements Gun {
    @Override
    public void fire(@NotNull Player player, BulletManager bulletManager) {

        int zoomLevel = player.getItemInMainHand().getTag(Tag.Integer("scoped"));

        Bullet bullet = new Bullet();
        bullet.setPlayer(player);
        bullet.setPos(player.getPosition().add(0, player.getEyeHeight()-0.6, 0));
        bullet.setInterval(1);
        bullet.setCanBreakBlock(false);
        bullet.setDamage(getDamage());
        bullet.setPredicate(
                (pos) -> {
                    Vec vec = pos.direction().normalize().mul(2);
                    pos = pos.add(vec.x(), vec.y(), vec.z());
                    GamePacket.sendPacket(player, getFiringParticle(pos));


                    return pos;
                });

        bullet.setOnEntityPredicate((pos, entity, bullet1) -> {
            ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                    Particle.LAVA,
                    pos.x(),
                    pos.y(),
                    pos.z(),
                    1,1,1,5
            );
            GamePacket.sendPacket(bullet.getInstance(), particlePacket);
            return true;
        });

        sendCooldownPacket(player);

        bulletManager.submitBullet(bullet, 200);

    }

    @Override
    public int getAmmo() {
        return 10;
    }

    @Override
    public ItemStack getRawItemStack() {
        return ItemStack.builder(Material.DIAMOND_PICKAXE)
                .displayName(Component.text("AWP", NamedTextColor.GOLD))
                .build()
                .withTag(Tag.Integer("scoped"), 0);
    }

    @Override
    public int getDamage() {
        return 20;
    }

    @Override
    public double getCoolDown() {
        return 40;
    }

    @Override
    public int getMaxFlyingDistance() {
        return 100;
    }

    @Override
    public int getEachAmmoReloadTime() {
        return 10;
    }

    @Override
    public ParticlePacket getFiringParticle(Pos pos) {
        return ParticleCreator.createParticlePacket(
                Particle.DUST,
                false,
                pos.x(),
                pos.y(),
                pos.z(),
                0,
                0,
                0,
                2,
                0,
                (writer) -> {
                    writer.writeFloat((float) Math.random());
                    writer.writeFloat((float) Math.random());
                    writer.writeFloat((float) Math.random());
                    writer.writeFloat(2F);
                    //try {
                        //writer.write(100);
                        //writer.write(100);
                        //writer.write(100);
                        //writer.write(3);
                    //} catch (IOException e) {
                    //    e.printStackTrace();
                    //}
                }

        );
    }
}
