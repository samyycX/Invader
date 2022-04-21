package Gun.particle;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;

public class GameParticle {

    /**
     * 给玩家发方块的粒子包
     * @param player 玩家
     * @param block 方块
     * @param blockPos 方块的坐标
     */
    public static void sendBlockParticle(Player player, Block block, Pos blockPos) {
        ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                Particle.BLOCK,
                false,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                0,
                0,
                0,
                block.stateId(),
                40,
                (writer) -> {
                    writer.write(block.stateId());
                }
        );
        player.sendPacket(particlePacket);
    }

}
