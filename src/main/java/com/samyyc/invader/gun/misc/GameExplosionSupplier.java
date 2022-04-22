package com.samyyc.invader.gun.misc;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Explosion;
import net.minestom.server.instance.ExplosionSupplier;
import net.minestom.server.instance.Instance;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameExplosionSupplier implements ExplosionSupplier {
    @Override
    public Explosion createExplosion(float centerX, float centerY, float centerZ, float strength, NBTCompound additionalData) {
        Explosion explosion = new Explosion(centerX, centerY, centerZ, strength) {
            @Override
            protected List<Point> prepare(Instance instance) {
                int[] x = new int[]{-2,-1,0,1,2};
                int[] z = new int[]{-2,-1,0,1,2};
                List<Point> points = new ArrayList<>();
                for (int y = 1; y < 3; y++) {
                    for (int k : x) {
                        for (int i : z) {
                            Pos pos = new Pos(getCenterX() + k, getCenterY() - y, getCenterZ() + i);
                            points.add(pos);
                        }
                    }
                }
                //apply(instance);
                return points;
            }
        };
        return explosion;
    }
}
