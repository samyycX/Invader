package Gun;

import Gun.GunImpl.AssaultRifle;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class GunsManager {

    private final List<Gun> guns = new ArrayList<>();

    public GunsManager() {
        guns.add(new AssaultRifle());
    }

    public void hookEvent(EventNode<Event> node) {
        node.addListener(PlayerUseItemEvent.class, e -> {

            e.getPlayer().getAcquirable().sync(player -> {
                ((Player) player).sendMessage("a");
            });

            String gunName = e.getItemStack().getTag(Tag.String("gun"));
            if (gunName != null) {
                for (Gun gun : guns) {
                    if (gun.getTag().equals(gunName)) {
                        gun.fire(e.getPlayer());
                        break;
                    }
                }
            }
        });
    }

}
