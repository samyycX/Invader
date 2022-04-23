package com.samyyc.invader.gun.util;


import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.block.BlockIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameUtil {

    public static List<Point> getLineOfSight(Player player, int maxDistance) {
        Instance instance = player.getInstance();
        if (instance == null) {
            return Collections.emptyList();
        } else {
            List<Point> blocks = new ArrayList();
            BlockIterator it = new BlockIterator(player, maxDistance);

            while(it.hasNext()) {
                Point position = it.next();
                blocks.add(position);
            }

            return blocks;
        }
    }

}
