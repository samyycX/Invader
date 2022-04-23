package com.samyyc.invader.game.singlemode;

import com.samyyc.invader.Main;
import com.samyyc.invader.game.IGameController;
import com.samyyc.invader.game.PlayerKillPlayerEvent;
import com.samyyc.invader.gun.misc.GameExplosionSupplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.provider.MinestomPlainTextComponentSerializerProvider;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.ClientPacketsHandler;
import net.minestom.server.scoreboard.Sidebar;

import java.util.*;

public class SingleGameController implements IGameController {

    private Instance instance;
    private Instance sourceInstance;
    private final Map<Player, Integer> players = new HashMap<>();
    private Player winner;
    private boolean closed = false;

    private static final int WINNER_SCORE = 10;

    @Override
    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void setSourceInstance(Instance instance) {
        this.sourceInstance = instance;
    }

    @Override
    public void addPlayer(Player player) {

        Random random = new Random();
        Pos pos = new Pos(random.nextInt(20), 42, random.nextInt(20));

        player.setInstance(this.instance, pos);
        players.put(player, 0);

        Sidebar sidebar  = new Sidebar(Component.text("计分板", NamedTextColor.GREEN));
        int line = 1;
        MinestomPlainTextComponentSerializerProvider provider = new MinestomPlainTextComponentSerializerProvider();
        for (Map.Entry<Player, Integer> entry : players.entrySet()) {
            sidebar.createLine(
                    new Sidebar.ScoreboardLine(
                            String.valueOf(line),
                            Component.text(provider.plainTextSimple().serialize(entry.getKey().getName()), NamedTextColor.AQUA),
                            entry.getValue()
                    )
            );
            line++;
        }
        sidebar.addViewer(player);
    }

    @Override
    public void removePlayer(Player player, Instance targetInstance) {
        this.players.remove(player);
        if (targetInstance != null) {
            player.setInstance(targetInstance);
        }
    }

    @Override
    public boolean containPlayer(Player player) {
        return players.containsKey(player);
    }

    @Override
    public void finishGame(Instance targetInstance) {

        for (Player player : players.keySet()) {
            player.showTitle(Title.title(Component.text("第一名是", NamedTextColor.RED)
                            .append(winner.getDisplayName().color(NamedTextColor.GREEN))
                    , Component.empty()));
            player.setInstance(targetInstance);
        }
        closed = true;
    }

    @Override
    public Instance getInstance() {
        return this.instance;
    }

    @Override
    public boolean checkIfEnable() {
        return players.size() >= 10;
    }

    protected void hookEvent() {
        this.instance.eventNode().addListener(PlayerKillPlayerEvent.class, e -> {
            int killCount = players.get(e.getKiller());
            if (++killCount == WINNER_SCORE) {
                winner = e.getKiller();
                finishGame(sourceInstance);
            } else {
                players.put(e.getKiller(), killCount);
                Sidebar sidebar  = new Sidebar(Component.text("计分板", NamedTextColor.GREEN));
                int line = 0;
                for (Map.Entry<Player, Integer> entry : players.entrySet()) {
                    sidebar.createLine(
                            new Sidebar.ScoreboardLine(
                                    String.valueOf(line),
                                    Component.text(killCount, NamedTextColor.RED),
                                    line
                            )
                    );
                    line++;
                }
                for (Player player : players.keySet()) {
                    sidebar.addViewer(player);
                }
            }
        });
    }

    @Override
    public boolean getClosed() {
        return closed;
    }
}
