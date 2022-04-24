package com.samyyc.invader.gun.bullet;

import com.samyyc.invader.util.Pair;
import net.minestom.server.coordinate.Pos;

@FunctionalInterface
public interface BulletOnTickPredicate {

    Pos run(Pos pos);

}
