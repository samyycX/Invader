package com.samyyc.invader.gun.bullet;

import net.minestom.server.Tickable;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class Bullet implements Tickable {

    private Pos pos;
    private Player player;
    private int interval;
    private boolean canBreakBlock;
    private float damage;


    private BulletOnTickPredicate predicate = pos1 -> pos1;
    private BulletOnBlockPredicate onBlockPredicate = (pos1, block, bullet) -> true;
    private BulletOnEntityPredicate onEntityPredicate = (pos1, entity, bullet) -> true;
    private BulletOnEndPredicate onEndPredicate = (pos1, bullet) -> {};

    private boolean ended = false;

    public Bullet(Player sender, Pos pos, int interval, boolean canBreakBlock, float damage, BulletOnTickPredicate predicate) {
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

        this.pos = this.predicate.run(pos);

        if (!player.getInstance().isChunkLoaded(pos)) {
            ended = true;
        }
    }

    public void callOnEntity(Entity entity) {
        ended = onEntityPredicate.run(pos, entity, this);
    }

    public void callOnBlock(Block block) {
        ended = onBlockPredicate.run(pos, block, this);
    }

    public void callOnEnd() {this.onEndPredicate.run(pos, this);}

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

    public void setPredicate(BulletOnTickPredicate predicate) {
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

    public void setOnBlockPredicate(BulletOnBlockPredicate onBlockPredicate) {
        this.onBlockPredicate = onBlockPredicate;
    }

    public void setOnEntityPredicate(BulletOnEntityPredicate onEntityPredicate) {
        this.onEntityPredicate = onEntityPredicate;
    }

    public void setOnEndPredicate(BulletOnEndPredicate onEndPredicate) {
        this.onEndPredicate = onEndPredicate;
    }

    public BulletOnEndPredicate getOnEndPredicate() {
        return onEndPredicate;
    }
}
