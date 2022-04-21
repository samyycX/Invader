package mob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;


public class CustomMob extends EntityCreature {

    public static final String EMPTY_BLOCK_CHAR = "□";
    public static final String FULL_BLOCK_CHAR = "■";

    public CustomMob(@NotNull EntityType entityType, int maxHealth, int attackDamage) {
        super(entityType);
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(maxHealth);
        getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(attackDamage);
        heal();
        setCustomName(getHealthBar(getMaxHealth(), getHealth()));
        setCustomNameVisible(true);
        eventNode().addListener(EntityDamageEvent.class, e -> {
            setCustomName(getHealthBar(getMaxHealth(), getHealth()));
        });
    }

    public Component getHealthBar(float maxHealth, float health) {
        final int charHealth = (int) Math.ceil((health / maxHealth) * getHealthBarLength());
        return Component.text()
                .append(Component.text("|", NamedTextColor.DARK_GRAY, TextDecoration.BOLD))
                .append(Component.text(FULL_BLOCK_CHAR.repeat(charHealth)+EMPTY_BLOCK_CHAR.repeat(getHealthBarLength() - charHealth), NamedTextColor.RED))
                .append(Component.text("|", NamedTextColor.DARK_GRAY, TextDecoration.BOLD))
                .build();
    }

    public int getHealthBarLength() {
        return 10;
    }
}
