package com.samyyc.invader.gun.misc;

import com.samyyc.invader.util.Pair;
import com.samyyc.invader.util.Tuple;
import net.minestom.server.MinecraftServer;
import net.minestom.server.Tickable;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.generator.UnitModifier;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class Bullet implements Tickable {

    private Pos pos;
    private Player player;
    private BulletPredicate<Pos> predicate;
    private int interval;
    private boolean canBreakBlock;
    private float damage;

    private boolean ended = false;

    public Bullet(Player sender, Pos pos, int interval, boolean canBreakBlock, float damage, BulletPredicate<Pos> predicate) {
        this.pos = pos;
        this.player = sender;
        this.predicate = predicate;
        this.interval = interval;
        this.canBreakBlock = canBreakBlock;
        this.damage = damage;
    }

    public Bullet() {

    }

    @Override
    public void tick(long time) {
        Pair<Pos, Boolean> pair = this.predicate.run(pos);

        this.pos = pair.K();
        this.ended = pair.V();
        if (!player.getInstance().isChunkLoaded(pos)) {
            ended = true;
        }
    }

    public boolean getEnded() {
        return ended;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isCanBreakBlock() {
        return canBreakBlock;
    }

    public Instance getInstance() {
        return this.player.getInstance();
    }

    public Pos getPos() {
        return pos;
    }

    public Player getPlayer() {
        return player;
    }

    public float getDamage() {
        return damage;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPredicate(BulletPredicate<Pos> predicate) {
        this.predicate = predicate;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setCanBreakBlock(boolean canBreakBlock) {
        this.canBreakBlock = canBreakBlock;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
