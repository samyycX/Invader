package Gun.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class GameData {

    public static final int ASSAULT_RIFLE_AMMO = 60;
    public static final double ASSAULT_RIFLE_COOLDOWN = 0.5;
    public static final int ASSAULT_RIFLE_MAX_DISTANCE = 60;
    public static final int ASSAULT_RIFLE_DAMAGE = 2;
    public static final ItemStack ASSAULT_RIFLE_ITEMSTACK = ItemStack
            .builder(Material.IRON_HOE)
            .displayName(Component.text("Ak-47突击步枪", NamedTextColor.RED))
            .build();


}
