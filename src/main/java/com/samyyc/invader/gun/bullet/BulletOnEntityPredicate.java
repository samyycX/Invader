package com.samyyc.invader.gun.bullet;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;

@FunctionalInterface
public interface BulletOnEntityPredicate {

    boolean run(Pos pos, Entity entity, Bullet bullet);

}
