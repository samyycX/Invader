package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.GunsManager;
import com.samyyc.invader.gun.bullet.BulletManager;
import com.samyyc.invader.gun.data.GameData;
import com.samyyc.invader.gun.entity.WitherSkullMissile;
import com.samyyc.invader.gun.packet.GamePacket;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class RPG implements Gun {
    @Override
    public void fire(@NotNull Player player, BulletManager bulletManager) {

        Entity skull = new Entity(EntityType.WITHER_SKULL);
        skull.setInstance(player.getInstance(), player.getPosition().add(0, player.getEyeHeight(), 0));
        skull.setGravity(0 , 0);
        Vec vec = player.getPosition().direction();
        vec = vec.mul(30);
        skull.setVelocity(vec);
        sendCooldownPacket(player);
        player.setItemInMainHand(player.getItemInMainHand().withTag(Tag.Long("lastRefresh"), System.currentTimeMillis()));

        MinecraftServer.getSchedulerManager().submitTask(
                () -> {
                    GamePacket.sendPacket(player, getFiringParticle(skull.getPosition()));

                    if (skull.getAliveTicks() >= (long) getMaxFlyingDistance() * MinecraftServer.TICK_PER_SECOND) {
                        skull.remove();
                        return TaskSchedule.stop();
                    }

                    if (skull.isOnGround()) {
                        Pos pos = skull.getPosition();
                        ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                                Particle.EXPLOSION,
                                pos.x(),
                                pos.y(),
                                pos.z(),
                                1,
                                1,
                                1,
                                10
                        );

                        GamePacket.sendPacket(player, particlePacket);

                        skull.remove();
                        return TaskSchedule.stop();
                    }

                    Collection<Entity> nearByEntities = skull.getInstance().getNearbyEntities(skull.getPosition(), Gun.HIT_RANGE_CHECK);
                    if (!nearByEntities.isEmpty()) {
                        AtomicBoolean targetIsPlayer = new AtomicBoolean(true);

                        nearByEntities.forEach(entity -> {
                            if (entity instanceof LivingEntity && entity != player) {
                                ((LivingEntity) entity).damage(DamageType.fromPlayer(player), getDamage());
                                targetIsPlayer.set(false);
                                GunsManager.callKillingEvent(player, entity, getDamage());
                            }
                        });
                        if (!targetIsPlayer.get()) return TaskSchedule.stop();
                    }



                    return TaskSchedule.tick(1);
                }, ExecutionType.SYNC
        );


    }

    @Override
    public int getAmmo() {
        return GameData.RPG_AMMO;
    }

    @Override
    public ItemStack getRawItemStack() {
        return GameData.RPG_ITEMSTACK;
    }

    @Override
    public int getDamage() {
        return GameData.RPG_DAMAGE;
    }

    @Override
    public double getCoolDown() {
        return GameData.RPG_COOLDOWN;
    }

    @Override
    public int getMaxFlyingDistance() {
        return GameData.RPG_MAX_EXIST;
    }

    @Override
    public int getEachAmmoReloadTime() {
        return 20;
    }

    @Override
    public ParticlePacket getFiringParticle(Pos pos) {
        return ParticleCreator.createParticlePacket(
                Particle.LAVA,
                pos.x(),
                pos.y(),
                pos.z(),
                0.5F,
                0.5F,
                0.5F,
                2
        );
    }
}
