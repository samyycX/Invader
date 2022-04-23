package com.samyyc.invader.game.meteoritemode;

import com.samyyc.invader.game.IGameController;
import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.misc.GameExplosionSupplier;
import com.samyyc.invader.gun.packet.GamePacket;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MeteoriteGameController implements IGameController {

    private Instance instance;
    private Instance sourceInstance;
    private Instance gamingInstance;
    private final Set<Player> players = new HashSet<>();
    private final Set<Player> alivePlayers = new HashSet<>();
    private boolean closed;

    @Override
    public void setInstance(Instance instance) {
        // 等待大厅
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40,
                    Block.POLISHED_ANDESITE);
        });
        this.instance = instance;
    }

    @Override
    public void setSourceInstance(Instance instance) {
        this.sourceInstance = instance;
    }

    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setInstance(instance, new Pos(0, 42, 0));

        if (players.size() > 0) {
            startGame();
        }
    }

    @Override
    public void removePlayer(Player player, @Nullable Instance targetInstance) {
        if (targetInstance != null) {
            player.setInstance(targetInstance);
        }
        this.players.remove(player);
    }

    @Override
    public boolean containPlayer(Player player) {
        return this.players.contains(player);
    }

    @Override
    public void finishGame(Instance targetInstance) {
        this.players.forEach(player -> player.setInstance(sourceInstance));
        closed = true;
    }

    @Override
    public Instance getInstance() {
        return instance;
    }

    @Override
    public boolean checkIfEnable() {
        return Objects.isNull(gamingInstance);
    }

    public void startGame() {

        alivePlayers.addAll(players);

        gamingInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        gamingInstance.setExplosionSupplier(new GameExplosionSupplier());
        gamingInstance.getWorldBorder().setCenter(0, 0);
        gamingInstance.getWorldBorder().setDiameter(50);
        gamingInstance.setTime(18000);
        gamingInstance.setTimeRate(0);
        gamingInstance.setGenerator(unit -> {
            unit.modifier().fillHeight(2, 20, Block.POLISHED_DEEPSLATE);
        });

        AtomicInteger counter = new AtomicInteger(5);
        Random random = new Random();
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (counter.get() != 0) {
                alivePlayers.forEach(player -> player.sendTitlePart(TitlePart.TITLE,
                        Component.text("离游戏开始还有 "+counter.get()+" 秒", NamedTextColor.GREEN)));
                counter.decrementAndGet();
                return TaskSchedule.seconds(1);
            } else {
                alivePlayers.forEach(player -> {
                    player.setInstance(gamingInstance, new Pos(random.nextInt(-20,20), 22, random.nextInt(-20,20)));
                    player.setGameMode(GameMode.ADVENTURE);
                });
                return TaskSchedule.stop();
            }
        }, ExecutionType.SYNC);


        gamingInstance.eventNode().addListener(PlayerDeathEvent.class, e -> {
            e.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("YOU DEAD", NamedTextColor.RED));
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            alivePlayers.remove(e.getPlayer());
        });

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            Entity skull = new Entity(EntityType.WITHER_SKULL);
            Pos pos2 = new Pos(random.nextInt(50)-25, 140, random.nextInt(50)-25);
            skull.setInstance(gamingInstance, pos2);
            //skull.setGravity(0 , 0);
            skull.setNoGravity(true);
            Vec vec = pos2.direction().withX(0).withZ(0).withY(-3).mul(10);
            skull.setVelocity(vec);

            MinecraftServer.getSchedulerManager().submitTask(
                    () -> {
                        GamePacket.sendPacket(gamingInstance, getFiringParticle(skull.getPosition()));

                        if (skull.isOnGround()) {
                            Pos pos = skull.getPosition();
                            ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                                    Particle.EXPLOSION,
                                    pos.x(),
                                    pos.y(),
                                    pos.z(),
                                    1,
                                    1,
                                    1,
                                    10
                            );


                            gamingInstance.explode(
                                    (float) pos.x(),
                                    (float) pos.y(),
                                    (float) pos.z(),
                                    6,
                                    null
                            );

                            GamePacket.sendPacket(gamingInstance, particlePacket);

                            skull.remove();
                            return TaskSchedule.stop();
                        }

                        return TaskSchedule.tick(1);
                    }, ExecutionType.SYNC
            );
            return TaskSchedule.tick(5);
        }, ExecutionType.SYNC);
    }

    public ParticlePacket getFiringParticle(Pos pos) {
        return ParticleCreator.createParticlePacket(
                Particle.LAVA,
                pos.x(),
                pos.y(),
                pos.z(),
                0.5F,
                0.5F,
                0.5F,
                3
        );
    }

    @Override
    public boolean getClosed() {
        return closed;
    }
}
