package com.samyyc.invader.gun;

import com.samyyc.invader.gun.GunImpl.AssaultRifle;
import com.samyyc.invader.gun.GunImpl.RPG;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GunsManager {

    private static final List<Gun> guns = new ArrayList<>();

    public static void init() {
        guns.add(new AssaultRifle());
        guns.add(new RPG());
    }

    public static void hookEvent(EventNode<Event> node) {
        node.addListener(PlayerUseItemEvent.class, e -> {

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

    @Nullable
    public static ItemStack getGunsItemstack(String tag) {
        for (Gun gun : guns) {
            if (gun.getTag().equals(tag)) {
                return gun.getItemStack();
            }
        }
        return null;
    }

}
