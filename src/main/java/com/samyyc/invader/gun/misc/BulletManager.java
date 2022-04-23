package com.samyyc.invader.gun.misc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.Tickable;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;

public class BulletManager {

    private static Map<Bullet, Long> bullets = new HashMap<>();

    public static void init() {
        MinecraftServer.getSchedulerManager().submitTask( () -> {
            Iterator<Map.Entry<Bullet, Long>> bulletIt = BulletManager.bullets.entrySet().iterator();
                while (bulletIt.hasNext()) {
                    Map.Entry<Bullet, Long> entry  = bulletIt.next();
                    Bullet bullet = entry.getKey();
                    if (!bullet.getEnded() && entry.getValue() > 0) {
                        if (entry.getValue() % bullet.getInterval() == 0) {
                            if (bullet.isCanBreakBlock()) {
                                bullet.getInstance().setBlock(bullet.getPos(), Block.AIR);
                            } else {
                                if (!bullet.getInstance().getBlock(bullet.getPos()).isAir()) {
                                    bulletIt.remove();
                                    continue;
                                }
                            }

                            Collection<Entity> nearByEntities = bullet.getInstance().getNearbyEntities(bullet.getPos(), 1);
                            if (!nearByEntities.isEmpty()) {
                                nearByEntities.forEach(entity -> {
                                    if (entity != bullet.getPlayer() && entity instanceof LivingEntity livingEntity) {
                                        livingEntity.damage(DamageType.fromPlayer(bullet.getPlayer()), bullet.getDamage());
                                    }
                                });
                                bulletIt.remove();
                            }
                            entry.getKey().tick(1);
                        }
                        bullets.put(entry.getKey(),entry.getValue() - 1);
                    } else {
                        System.out.println(1);
                        bulletIt.remove();
                    }
                }
                return TaskSchedule.millis(1);
         }, ExecutionType.SYNC
        );
    }

    public static void submitBullet(Bullet bullet, long maxLivingTime) {
        bullets.put(bullet, maxLivingTime);
    }

}
