package Gun;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import org.jetbrains.annotations.NotNull;

public interface Gun {

    public static double VECTOR_TWO_POINT_INTERVAL = 0.6;
    public static double HIT_RANGE_CHECK = 2;

    void fire(@NotNull Player player);

    int getAmmo();

    ItemStack getItemStack();

    int getDamage();

    double getCoolDown();

    int getMaxFlyingDistance();

    default Sound getHitSound() {
        Sound hitSound = Sound.sound(
                Key.key("entity.arrow.hit_player"),
                Sound.Source.AMBIENT,
                10,
                0
        );
        return hitSound;
    };

    default Sound getFiringSound() {
        Sound firingSound = Sound.sound(
                Key.key("block.piston.contract"),
                Sound.Source.AMBIENT,
                20,
                2
        );
        return firingSound;
    };

    default ParticlePacket getFiringParticle(Pos pos) {
        ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                Particle.GLOW,
                false,
                pos.x(),
                pos.y(),
                pos.z(),
                0F,
                0F,
                0F,
                Block.GRASS_BLOCK.stateId(),
                0,
                null
        );
        return particlePacket;
    }

    String getTag();

}
