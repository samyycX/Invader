package com.samyyc.invader.gun.GunImpl;

import com.samyyc.invader.gun.Gun;
import com.samyyc.invader.gun.data.GameData;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.entity.fakeplayer.FakePlayerOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RPG implements Gun {
    @Override
    public void fire(@NotNull Player player) {
        FakePlayerOption option = new FakePlayerOption();
        option.setInTabList(false);
        FakePlayer.initPlayer(UUID.randomUUID(), "SKULL", option, (fake) -> {
            fake.setInvisible(true);
            ItemStack skull = ItemStack.builder(Material.PLAYER_HEAD)
                            .build()
                                    .withTag(Tag.String("SkullOwner"), "MHF_Creeper");
            fake.getInventory().setHelmet(skull);

            Vec vec = player.getPosition().direction();
            fake.setVelocity(vec);
        });
    }

    @Override
    public int getAmmo() {
        return GameData.RPG_AMMO;
    }

    @Override
    public ItemStack getItemStack() {
        return GameData.RPG_ITEMSTACK.withTag(Tag.String("gun"), getTag());
    }

    @Override
    public int getDamage() {
        return GameData.RPG_DAMAGE;
    }

    @Override
    public double getCoolDown() {
        return GameData.RPG_COOLDOWN;
    }

    @Override
    public int getMaxFlyingDistance() {
        return GameData.RPG_MAX_DISTANCE;
    }

    @Override
    public String getTag() {
        return "rpg";
    }
}
