package util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.chunk.ChunkUtils;

public class ChunkHandler {

    private static GlobalEventHandler eventHandler;

    public static void init(GlobalEventHandler eventHandler, Instance instance) {
        ChunkHandler.eventHandler = eventHandler;
        /*
        eventHandler.addListener(PlayerBlockPlaceEvent.class, e -> {
            e.getInstance().saveChunkToStorage(e.getInstance().getChunkAt(e.getBlockPosition()));
        });
        eventHandler.addListener(PlayerBlockBreakEvent.class, e-> {
            e.getInstance().saveChunkToStorage(e.getInstance().getChunkAt(e.getBlockPosition()));
        });
        eventHandler.addListener(PlayerMoveEvent.class, e -> {
            Chunk chunk = instance.getChunkAt(e.getNewPosition());
            if (!chunk.isLoaded()) {
                instance.loadChunk(e.getNewPosition());
            }
        });

         */

    }

}
