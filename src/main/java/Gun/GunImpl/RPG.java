package Gun.GunImpl;

import Gun.Gun;
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
        return 0;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public double getCoolDown() {
        return 0;
    }

    @Override
    public int getMaxFlyingDistance() {
        return 0;
    }

    @Override
    public String getTag() {
        return null;
    }
}
