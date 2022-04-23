package com.samyyc.invader.gun.packet;

import com.samyyc.invader.gun.Gun;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;

public class GamePacket {

    public static void sendBlockBreakAnimationPacket(Player player, Pos blockPos, int breakStage) {
        BlockBreakAnimationPacket blockBreakAnimationPacket = new BlockBreakAnimationPacket(
                (int) (Math.random()*100000),
                blockPos,
                (byte) breakStage
        );
        sendPacket(player, blockBreakAnimationPacket);
    }

    public static void sendPacket(Player player, SendablePacket packet) {
        sendPacket(player.getInstance(), packet);
    }

    public static void sendPacket(Instance instance, SendablePacket packet) {
        instance.getPlayers().forEach(player -> {
            player.sendPacket(packet);
        });
    }

    public static void drawLine(Pos from, Pos to, double space, Particle particle, Player player) {
        double distance = from.distance(to);
        Vec p1 = from.asVec();
        Vec p2 = to.asVec();
        Vec vec = p2.sub(p1).normalize().mul(space);
        double length = 0;
        for (; length < distance; p1.add(vec)) {
            ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                    particle,
                    p1.x(),
                    p1.y(),
                    p1.z(),
                    0,
                    0,
                    0,
                    0
            );
            sendPacket(player, particlePacket);
            length+=space;
        }
    }



}

