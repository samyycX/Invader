package com.samyyc.invader.gun.packet;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;

public class GamePacket {

    public static void sendBlockBreakAnimationPacket(Player player, Pos blockPos, int breakStage) {
        BlockBreakAnimationPacket blockBreakAnimationPacket = new BlockBreakAnimationPacket(
                (int) (Math.random()*100000),
                blockPos,
                (byte) breakStage
        );
        player.sendPacket(blockBreakAnimationPacket);
    }


}
