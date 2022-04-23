package com.samyyc.invader.gun.misc;

import com.samyyc.invader.util.Pair;
import com.samyyc.invader.util.Tuple;

@FunctionalInterface
public interface BulletPredicate<U> {

    Pair<U,Boolean> run(U u);

}
