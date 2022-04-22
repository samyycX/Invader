package com.samyyc.invader.game;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public record PlayerKillPlayerEvent(Player killer, Player dead,
                                    Instance instance) implements PlayerEvent, InstanceEvent {

    @Override
    public @NotNull
    Player getPlayer() {
        return dead;
    }

    public Player getKiller() {
        return killer;
    }

    @Override
    public @NotNull
    Player getEntity() {
        return dead;
    }

    @Override
    public @NotNull
    Instance getInstance() {
        return instance;
    }
}
