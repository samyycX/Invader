package com.samyyc.invader.game;

import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.instance.Instance;

import java.util.List;
import java.util.Map;

public interface IGameManager {

    IGameController  newGame();

    List<IGameController> getAllRunningGame();

    void finishGame(int gameId);

    void join(Player player);

    void leave(Player player);

    default void hookEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerDisconnectEvent.class, e -> {
            for (IGameController gameController : getAllRunningGame()) {
                if (gameController.containPlayer(e.getPlayer())) {
                    gameController.removePlayer(e.getPlayer(), null);
                }
            }
        });
    }

}
