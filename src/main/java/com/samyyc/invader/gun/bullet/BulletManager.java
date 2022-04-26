package com.samyyc.invader.gun.bullet;

import com.samyyc.invader.gun.GunsManager;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BulletManager {

    private ConcurrentHashMap<Bullet, Long> bullets = new ConcurrentHashMap<>();
    private Instance instance;

    public BulletManager(Instance instance) {
        this.instance = instance;
    }

    public void init() {
        MinecraftServer.getSchedulerManager().submitTask( () -> {
           Iterator<Map.Entry<Bullet, Long>> bulletIt = bullets.entrySet().iterator();

                while (bulletIt.hasNext()) {
                    Map.Entry<Bullet, Long> entry  = bulletIt.next();
                    Bullet bullet = entry.getKey();

                    if (bullet.getEnded()) {
                        bulletIt.remove();
                        continue;
                    }

                    if (!bullet.getEnded() && entry.getValue() > 0) {
                        if (entry.getValue() % bullet.getInterval() == 0) {
                            if (bullet.isCanBreakBlock()) {
                                bullet.getInstance().setBlock(bullet.getPos(), Block.AIR);
                            } else {
                                if (!bullet.getInstance().getBlock(bullet.getPos()).isAir()) {
                                    bullet.callOnBlock(bullet.getInstance().getBlock(bullet.getPos()));
                                }
                            }

                            Collection<Entity> entities = instance.getNearbyEntities(bullet.getPos(), 1.6);
                            for (Entity entity : entities) {
                                if (entity != bullet.getPlayer() && entity instanceof LivingEntity livingEntity) {
                                    livingEntity.damage(DamageType.fromPlayer(bullet.getPlayer()), bullet.getDamage());
                                    bullet.callOnEntity(livingEntity);
                                    GunsManager.callKillingEvent(bullet.getPlayer(), entity, bullet.getDamage());
                                }
                            }

                        }
                        bullets.put(bullet,entry.getValue() - 1);
                        bullet.tick(1);
                    } else {
                        bulletIt.remove();
                    }

                }


                return TaskSchedule.millis(1);
         }, ExecutionType.SYNC
        );
    }

    public void submitBullet(Bullet bullet, long maxLivingTime) {
        bullets.put(bullet, maxLivingTime);
    }

}
