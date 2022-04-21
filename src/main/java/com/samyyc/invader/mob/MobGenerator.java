package com.samyyc.invader.mob;

import com.samyyc.invader.mob.CustomMob;

import java.util.List;

@FunctionalInterface
public interface MobGenerator {

    List<CustomMob> generate();

}
