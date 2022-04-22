package com.samyyc.invader.command.vanilla;

import com.samyyc.invader.mob.TestMob;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.time.TimeUnit;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class CTest2 extends Command {

    private final InstanceContainer instance;
    public CTest2(InstanceContainer instance) {
        super("test2","test2");
        this.instance = instance;

        addSyntax((sender, context) -> {
            TestMob mob = new TestMob();
            mob.setInstance(instance);
            mob.spawn();
            mob.teleport(new Pos(0, 41, 0));
        });
    }}
