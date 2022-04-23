package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.misc.Bullet;
import com.samyyc.invader.gun.misc.BulletManager;
import com.samyyc.invader.gun.packet.GamePacket;
import com.samyyc.invader.gun.util.GameUtil;
import com.samyyc.invader.util.Pair;
import com.samyyc.invader.util.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.block.BlockIterator;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AWP implements Gun {
    @Override
    public void fire(@NotNull Player player) {

        int zoomLevel = player.getItemInMainHand().getTag(Tag.Integer("scoped"));

        Bullet bullet = new Bullet();
        bullet.setPlayer(player);
        bullet.setPos(player.getPosition());
        bullet.setInterval(1);
        bullet.setCanBreakBlock(true);
        bullet.setDamage(getDamage());
        bullet.setPredicate(
                (pos) -> {
                    Vec vec = pos.direction().normalize().mul(2);
                    pos = pos.add(vec.x(), vec.y(), vec.z());
                    GamePacket.sendPacket(player, getFiringParticle(pos));


                    return Pair.of(pos, false);
                });


        BulletManager.submitBullet(bullet, 200);

        /*
        Entity target = player.getLineOfSightEntity(0.2, null);

        if (target instanceof LivingEntity entity) {
            entity.damage(DamageType.fromPlayer(player), getDamage());
            GamePacket.drawLine(player.getPosition(), entity.getPosition(), 0.2, Particle.END_ROD, player);
            callKillingEvent(player, entity, getDamage());
            System.out.println(1);
        } else {
            Vec vec = player.getPosition().direction();
            GameUtil.getLineOfSight(player, getMaxFlyingDistance()).forEach(point -> {
                Pos pos = new Pos(point.x(), point.y(), point.z());
                System.out.println(pos);
                GamePacket.sendPacket(player, getFiringParticle(pos));
            });
        }

         */


        /*
        MinecraftServer.getSchedulerManager().submitTask(
                () -> {
                    if (counter.get() >= getMaxFlyingDistance()) {
                        return TaskSchedule.stop();
                    }

                    Vec vec = originVec.normalize().mul(counter.get()*4);
                    Pos pos = originPos.add(vec.x(), vec.y(), vec.z());
                    GamePacket.sendPacket(player, getFiringParticle(pos));
                    counter.getAndIncrement();
                    return TaskSchedule.tick(1);
                },
                ExecutionType.SYNC
        );

         */

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
                Particle.END_ROD,
                pos.x(),
                pos.y(),
                pos.z(),
                0,
                0,
                0,
                0
        );
    }
}
