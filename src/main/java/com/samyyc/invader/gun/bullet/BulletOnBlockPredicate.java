package com.samyyc.invader.gun.bullet;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

@FunctionalInterface
public interface BulletOnBlockPredicate {

    boolean run(Pos pos, Block block);

}
