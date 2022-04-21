import Gun.Gun;
import Gun.GunImpl.AssaultRifle;
import Gun.data.BlockBreakStage;
import command.vanilla.CGamemode;
import command.vanilla.CTeleport;
import command.vanilla.CTest;
import command.vanilla.CTest2;
import feature.Features;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.map.MapColors;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.utils.identity.NamedAndIdentified;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;
import util.ChunkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Main {

    public static InstanceContainer instance;

    public static void main(String[] args) {
        // 初始化服务器
        MinecraftServer server = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        DimensionTypeManager manager = new DimensionTypeManager();
        DimensionType a = DimensionType.builder(NamespaceID.from("minecraft:overworld")).ultrawarm(false).natural(true).piglinSafe(false).respawnAnchorSafe(false).bedSafe(true).raidCapable(true).skylightEnabled(true).ceilingEnabled(false).fixedTime((Long)null).ambientLight(16.0F).height(384).minY(-64).logicalHeight(384).infiniburn(NamespaceID.from("minecraft:infiniburn_overworld")).build();
        manager.addDimension(a);

        // 创建实例
        Main.instance = instanceManager.createInstanceContainer(a);
        BlockBreakStage.blockBreakStage.put(instance, new HashMap<>());

        // 设置区块生成器
        instance.setGenerator(unit -> {
            // 填充Y坐标轴 0-40 为草方块
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
        });

        // 添加一个事件处理器
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        // 玩家进服监听器
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            // 设置此任务的生成镜像
            event.setSpawningInstance(instance);
            // 设置玩家的出生地点
            player.setRespawnPoint(new Pos(0, 42, 0));


        });

        // 注册枪械
        Gun gun = new AssaultRifle();
        gun.register(instance);

        ChunkHandler.init(globalEventHandler, instance);


        // 注册命令
        MinecraftServer.getCommandManager().register(new CGamemode());
        MinecraftServer.getCommandManager().register(new CTest(instance));
        MinecraftServer.getCommandManager().register(new CTeleport());
        MinecraftServer.getCommandManager().register(new CTest2(instance));

        // 用户获取服务器列表时返回内容监听器
        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            ResponseData data = event.getResponseData();
            data.addEntry(NamedAndIdentified.named("Minestom Server"));
        });

        globalEventHandler.addListener(PlayerMoveEvent.class, e -> {
            for (Chunk chunk : instance.getChunks()) {
                if (chunk.getViewers().isEmpty()) {
                    instance.unloadChunk(chunk);
                }
            }
        });


        Features.combat().hook(EventNode.class.cast(instance.eventNode()));

        server.start("127.0.0.1", 25565);
    }
}
