package command.vanilla;

import Gun.Gun;
import Gun.GunImpl.AssaultRifle;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemMetaView;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.MapMeta;
import net.minestom.server.map.MapColors;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;
import net.minestom.server.network.packet.server.play.MapDataPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.TestOnly;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@TestOnly
public class CTest extends Command {

    private final InstanceContainer instance;
    public CTest(InstanceContainer instance) {
        super("test","test");
        this.instance = instance;

        addSyntax((sender, context) -> {

            Player player = (Player) sender;

            Gun rifle = new AssaultRifle();
            player.getInventory().addItemStack(rifle.getItemStack());

            ItemStack map = ItemStack.builder(Material.FILLED_MAP)
                    .displayName(Component.text("test", NamedTextColor.GREEN))
                    .build()
                    .withTag(Tag.Integer("map"), 10);
            DirectFramebuffer directFramebuffer = new DirectFramebuffer();
            directFramebuffer.set(0, 0, MapColors.ICE.baseColor());
            player.sendPackets(directFramebuffer.preparePacket(10));
            player.getInventory().addItemStack(map);

        });
    }}
