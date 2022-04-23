package com.samyyc.invader.game.meteoritemode;

import com.samyyc.invader.Main;
import com.samyyc.invader.game.IGameController;
import com.samyyc.invader.game.IGameManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.resourcepack.ResourcePack;

import java.util.*;

public class MeteoriteGameManager implements IGameManager {

    private final List<IGameController> runningGames = new ArrayList<>();
    private final Map<Player, Integer> allGamingPlayer = new HashMap<>();

    @Override
    public IGameController newGame() {
        IGameController gameController = new MeteoriteGameController();
        gameController.setInstance(MinecraftServer.getInstanceManager().createInstanceContainer());
        gameController.setSourceInstance(Main.instance);
        runningGames.add(gameController);
        return gameController;
    }

    @Override
    public List<IGameController> getAllRunningGame() {
        return runningGames;
    }

    @Override
    public void finishGame(int gameId) {
        runningGames.get(gameId).finishGame(Main.instance);
        runningGames.remove(gameId);
    }

    @Override
    public void join(Player player) {
        Iterator<IGameController> it = runningGames.iterator();
        int i = 0;
        while (it.hasNext()) {
            IGameController game = it.next();
            if (game.getClosed()) {
                it.remove();
            }
            if (game.checkIfEnable()) {
                game.addPlayer(player);
                allGamingPlayer.put(player, i);
            }
            i++;
        }
    }

    @Override
    public void leave(Player player) {
        int gameId = allGamingPlayer.get(player);
        runningGames.get(gameId).removePlayer(player, Main.instance);
        player.setGameMode(GameMode.ADVENTURE);
        allGamingPlayer.remove(player);
    }
}
