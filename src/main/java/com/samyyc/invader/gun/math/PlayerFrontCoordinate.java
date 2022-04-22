package com.samyyc.invader.gun.math;

import net.minestom.server.coordinate.Pos;

public class PlayerFrontCoordinate {

    private Pos zeroDot;
    private double rotateAngle;

    public PlayerFrontCoordinate(Pos playerLocation) {
        // 旋转的角度
        rotateAngle = playerLocation.yaw();
        zeroDot = playerLocation;
        zeroDot = zeroDot.withPitch(0);// 重设仰俯角
        zeroDot = zeroDot.add(zeroDot.direction().mul(0.6)); // 使原点与玩家有一点点距离
    }

    public Pos getZeroDot() {
        return zeroDot;
    }

    public Pos newLocation(double x, double y) {
        return rotateLocationAboutPoint(zeroDot.add(-x, y, 0), rotateAngle, zeroDot);
    }

    public static Pos rotateLocationAboutPoint(Pos Pos, double angle, Pos point) {
        double radians = Math.toRadians(angle);
        double dx = Pos.x() - point.x();
        double dz = Pos.z() - point.z();

        double newX = dx * Math.cos(radians) - dz * Math.sin(radians) + point.x();
        double newZ = dz * Math.cos(radians) + dx * Math.sin(radians) + point.z();
        return new Pos(newX, Pos.y(), newZ);
    }
}