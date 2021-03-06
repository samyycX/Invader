package com.samyyc.invader.game;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.Nullable;

public interface IGameController {

    void setInstance(Instance instance);

    void setSourceInstance(Instance instance);

    void addPlayer(Player player);

    void removePlayer(Player player, @Nullable Instance targetInstance);

    boolean containPlayer(Player player);

    void finishGame(Instance targetInstance);

    Instance getInstance();

    boolean checkIfEnable();

    boolean getClosed();

}
