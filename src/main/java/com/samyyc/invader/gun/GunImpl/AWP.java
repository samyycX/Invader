package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.packet.GamePacket;
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
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.HTML;
import java.util.concurrent.atomic.AtomicInteger;

public class AWP implements Gun {
    @Override
    public void fire(@NotNull Player player) {

        int zoomLevel = player.getItemInMainHand().getTag(Tag.Integer("scoped"));

        double offsetX = 0;
        if (zoomLevel == 0) offsetX = 5;

        AtomicInteger counter = new AtomicInteger(0);
        Vec originVec = player.getPosition().direction();
        Pos originPos = player.getPosition();

        MinecraftServer.getSchedulerManager().submitTask(
                () -> {
                    if (counter.get() >= getMaxFlyingDistance()) {
                        return TaskSchedule.stop();
                    }

                    Vec vec = originVec.normalize().mul(counter.get()*2);
                    Pos pos = originPos.add(vec.x(), vec.y(), vec.z());
                    GamePacket.sendPacket(player, getFiringParticle(pos));
                    counter.getAndIncrement();
                    return TaskSchedule.tick(1);
                },
                ExecutionType.SYNC
        );

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
