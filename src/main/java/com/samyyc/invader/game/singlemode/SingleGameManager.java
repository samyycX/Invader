package com.samyyc.invader.game.singlemode;

import com.samyyc.invader.Main;
import com.samyyc.invader.game.IGameController;
import com.samyyc.invader.game.IGameManager;
import com.samyyc.invader.gun.GunsManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

import java.util.*;

public class SingleGameManager implements IGameManager {

    private final List<IGameController> RUNNING_GAME = new ArrayList<>();
    private final Map<Player, Integer> PLAYER_MAP = new HashMap<>();

    @Override
    public IGameController newGame() {
        int id = RUNNING_GAME.size();
        SingleGameController gameController = new SingleGameController();
        gameController.setInstance(MinecraftServer.getInstanceManager().createInstanceContainer());
        gameController.setSourceInstance(Main.instance);
        RUNNING_GAME.add(gameController);

        return RUNNING_GAME.get(id);
    }

    @Override
    public List<IGameController> getAllRunningGame() {
        return this.RUNNING_GAME;
    }

    @Override
    public void finishGame(int gameId) {
        RUNNING_GAME.get(gameId).finishGame(Main.instance);
    }

    @Override
    public void join(Player player) {
        if (getAllRunningGame().size() == 0) {
            newGame().addPlayer(player);
            PLAYER_MAP.put(player, 0);
            return;
        }
        for (int i = 0; i < RUNNING_GAME.size(); i++) {
            IGameController game = RUNNING_GAME.get(i);
            if (!game.checkIfEnable()) {
                game.addPlayer(player);
                PLAYER_MAP.put(player, i);
                return;
            }
        }
        newGame().addPlayer(player);
    }

    @Override
    public void leave(Player player) {
        RUNNING_GAME.get(PLAYER_MAP.get(player)).removePlayer(player, Main.instance);
    }
}
