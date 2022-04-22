package com.samyyc.invader.gun.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.CombinedAttackGoal;
import net.minestom.server.entity.ai.goal.FollowTargetGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class WitherSkullMissile extends EntityCreature {
    public WitherSkullMissile() {
        super(EntityType.WITHER_SKULL);
        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new FollowTargetGoal(this, Duration.of(100, TimeUnit.SECOND)))
                        .build()
        );

    }
}
