package Gun.GunImpl;

import Gun.Gun;
import Gun.data.BlockBreakStage;
import Gun.data.GameData;
import Gun.packet.GamePacket;
import Gun.particle.GameParticle;
import Gun.sound.GameSound;
import com.sun.tools.javac.Main;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.tag.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssaultRifle implements Gun {

    private final int MAX_DISTANCE = 100;
    private final int AMMO = 60;
    private final int DAMAGE = 2;
    private final double COOLDOWN = 0.5;

    @Override
    public void fire(Player player) {
        // 获取玩家当前朝向的向量
        Vec vec = player.getPosition().direction();

        // 射程
        player.playSound(getFiringSound());
        for (int i = 1; i <= getMaxFlyingDistance(); i++) {
            // 向量增加
            Vec vec1 = vec.normalize().mul(Gun.VECTOR_TWO_POINT_INTERVAL*i);
            // 转换成坐标
            Pos pos = player.getPosition().add(vec1.x(), vec1.y()+player.getEyeHeight()-0.4, vec1.z());

            Instance instance = player.getInstance();
            // 判断是否击中实体
            if (!instance.getNearbyEntities(pos, Gun.HIT_RANGE_CHECK).isEmpty()) {
                // 击中的是否为发射者本人
                AtomicBoolean targetIsPlayer = new AtomicBoolean(true);
                // 扣除血量
                instance.getNearbyEntities(pos, Gun.HIT_RANGE_CHECK).forEach(entity -> {
                    if (entity.getPosition() != player.getPosition() && entity instanceof LivingEntity livingEntity) {
                        targetIsPlayer.set(false);
                        livingEntity.damage(DamageType.fromPlayer(player), 1);

                        player.playSound(GameSound.HIT_SOUND);
                    }
                });
                // 如果击中了发射者以外的实体，则阻止子弹继续飞行
                // 否则跳过
                if (!targetIsPlayer.get()) {
                    break;
                }
            }

            // 如果子弹击中了方块
            if (!instance.getBlock(pos).isAir()) {
                // 获取该方块
                Block block = instance.getBlock(pos);

                // 把方块坐标取整，方便获取Main方法中的方块破坏程度表
                Pos pos1 = new Pos(pos.blockX(), pos.blockY(), pos.blockZ());

                // 获取方块损坏程度表
                Map<Pos, Integer> blockBreakStage = BlockBreakStage.blockBreakStage.get(player.getInstance());
                int breakStage = 1;
                if (blockBreakStage.containsKey(pos1)) {
                    breakStage += blockBreakStage.get(pos1);
                }
                blockBreakStage.put(pos1, breakStage);

                // 如果损坏程度已经达到最大，则破坏掉该方块
                if (breakStage == 9) {
                    player.getInstance().setBlock(pos1, Block.AIR);
                    break;
                }

                // 发方块损坏特效，和方块损坏粒子
                GamePacket.sendBlockBreakAnimationPacket(player, pos1, breakStage);
                GameParticle.sendBlockParticle(player, block, pos1);

                break;
            }


            player.sendPacket(getFiringParticle(pos));
        }
    }

    @Override
    public int getAmmo() {
        return GameData.ASSAULT_RIFLE_AMMO;
    }

    @Override
    public ItemStack getItemStack() {
        return GameData.ASSAULT_RIFLE_ITEMSTACK.withTag(Tag.String("gun"), getTag());
    }

    @Override
    public int getDamage() {
        return GameData.ASSAULT_RIFLE_DAMAGE;
    }

    @Override
    public double getCoolDown() {
        return GameData.ASSAULT_RIFLE_COOLDOWN;
    }

    @Override
    public int getMaxFlyingDistance() {
        return GameData.ASSAULT_RIFLE_MAX_DISTANCE;
    }

    @Override
    public String getTag() {
        return "assault_rifle";
    }
}
