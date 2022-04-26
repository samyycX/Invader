package com.samyyc.invader.gun.bullet;

import net.minestom.server.coordinate.Pos;

@FunctionalInterface
public interface BulletOnEndPredicate {

    void run(Pos pos, Bullet bullet);
}
