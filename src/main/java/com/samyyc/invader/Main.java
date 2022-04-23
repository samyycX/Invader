package com.samyyc.invader;

import com.samyyc.invader.command.vanilla.*;
import com.samyyc.invader.feature.Features;
import com.samyyc.invader.game.IGameManager;
import com.samyyc.invader.game.meteoritemode.MeteoriteGameManager;
import com.samyyc.invader.game.singlemode.SingleGameManager;
import com.samyyc.invader.gun.GunsManager;
import com.samyyc.invader.gun.data.BlockBreakStage;
import com.samyyc.invader.gun.bullet.BulletManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.utils.identity.NamedAndIdentified;
import net.minestom.server.world.DimensionTypeManager;

import java.util.HashMap;

public class Main {

    public static InstanceContainer instance;

    public static IGameManager gameManager;
    public static MeteoriteGameManager meteoriteGameManager;

    public static void main(String[] args) {
        // 初始化服务器
        MinecraftServer server = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        DimensionTypeManager manager = new DimensionTypeManager();
        //DimensionType a = DimensionType.builder(NamespaceID.from("minecraft:overworld")).ultrawarm(false).natural(true).piglinSafe(false).respawnAnchorSafe(false).bedSafe(true).raidCapable(true).skylightEnabled(true).ceilingEnabled(false).fixedTime((Long)null).ambientLight(16.0F).height(384).minY(-64).logicalHeight(384).infiniburn(NamespaceID.from("minecraft:infiniburn_overworld")).build();
        //manager.addDimension(a);

        // 创建实例
        Main.instance = instanceManager.createInstanceContainer();
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
        GunsManager.init();
        GunsManager.hookEvent(EventNode.class.cast(instance.eventNode()));


        // 注册命令
        MinecraftServer.getCommandManager().register(new CGamemode());
        MinecraftServer.getCommandManager().register(new CTest(instance));
        MinecraftServer.getCommandManager().register(new CTeleport());
        MinecraftServer.getCommandManager().register(new CTest2(instance));
        MinecraftServer.getCommandManager().register(new CTime());

        // 用户获取服务器列表时返回内容监听器
        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            ResponseData data = event.getResponseData();
            data.addEntry(NamedAndIdentified.named("Minestom Server"));
        });

        gameManager = new SingleGameManager();
        gameManager.newGame();

        meteoriteGameManager = new MeteoriteGameManager();
        meteoriteGameManager.newGame();

        BulletManager.init();

        Features.combat().hook(EventNode.class.cast(instance.eventNode()));

        server.start("0.0.0.0", 25565);
    }
}
