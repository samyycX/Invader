package com.samyyc.invader.gun.bullet;

import com.samyyc.invader.gun.GunsManager;
import com.samyyc.invader.gun.util.GameUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class BulletManagerV2 {

    public static void addBullet(Bullet bullet) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {

            Collection<Entity> nearByEntities = bullet.getInstance().getNearbyEntities(bullet.getPos(), 2);
            for (Entity entity : nearByEntities) {
                if (!entity.equals(bullet.getPlayer()) && entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(DamageType.fromPlayer(bullet.getPlayer()), bullet.getDamage());
                    GunsManager.callKillingEvent(bullet.getPlayer(), livingEntity, bullet.getDamage());
                    bullet.callOnEntity(entity);
                }
            }

            if (!bullet.getInstance().isChunkLoaded(bullet.getPos())) {
                bullet.getInstance().loadChunk(bullet.getPos());
                bullet.callOnEnd();
                return TaskSchedule.stop();

            }
            Block block = bullet.getInstance().getBlock(bullet.getPos());
            if (!block.isAir()) {
                bullet.callOnBlock(block);
            }

            if (bullet.getEnded()) {
                bullet.callOnEnd();
                return TaskSchedule.stop();
            }

            bullet.tick(1);
            return TaskSchedule.millis(bullet.getInterval());

        });
    }

}
