package com.samyyc.invader.gun;

import com.samyyc.invader.Main;
import com.samyyc.invader.gun.GunImpl.AWP;
import com.samyyc.invader.gun.GunImpl.AssaultRifle;
import com.samyyc.invader.gun.GunImpl.RPG;
import com.samyyc.invader.gun.GunImpl.TestRPG;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.server.play.EffectPacket;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GunsManager {

    private static final Map<String, Gun> guns = new HashMap<>();

    public static void init() {
        Gun rifle = new AssaultRifle();
        Gun rpg = new RPG();
        Gun awp = new AWP();
        Gun testrpg = new TestRPG();
        guns.put(rifle.getTag(), rifle);
        guns.put(rpg.getTag(), rpg);
        guns.put(awp.getTag(), awp);
        guns.put(testrpg.getTag(), testrpg);
    }

    public static void hookEvent(EventNode<InstanceEvent> node) {
        node.addListener(PlayerUseItemEvent.class, e -> {

            String gunName = e.getItemStack().getTag(Tag.String("gun"));
            if (gunName != null) {
                Gun gun = findGun(gunName);
                if (!e.getItemStack().getTag(Tag.Boolean("reloading"))) {
                    if (gun.hasAmmo(e.getPlayer())) {
                        if ((System.currentTimeMillis() - e.getPlayer().getItemInMainHand().getTag(Tag.Long("lastRefresh"))) < (gun.getCoolDown() * MinecraftServer.TICK_PER_SECOND)) {
                            return;
                        }

                        gun.fire(e.getPlayer());
                        e.getPlayer().setItemInMainHand(e.getItemStack()
                                .withTag(
                                    Tag.Integer("ammo"),
                                    e.getItemStack().getTag(Tag.Integer("ammo")) - 1
                                )
                                .withTag(
                                        Tag.Long("lastRefresh"),
                                        System.currentTimeMillis()
                                )
                        );
                    } else {
                        gun.reloadAmmo(e.getPlayer());
                    }
                }
            }
        });

        node.addListener(PlayerSwapItemEvent.class, e -> {
            e.setCancelled(true);
            boolean reload = false;
            ItemStack mainHand = e.getOffHandItem();
            if (mainHand.hasTag(Tag.String("gun"))) {
                reload = true;
                e.getPlayer().setItemInMainHand(mainHand);
                String gunName = mainHand.getTag(Tag.String("gun"));
                findGun(gunName).reloadAmmo(e.getPlayer());
            }
            if (!reload) e.setCancelled(false);
        });

        node.addListener(PlayerHandAnimationEvent.class, e -> {
            e.getPlayer().addEffect(
                    new Potion(
                            PotionEffect.SLOWNESS,
                            (byte) 100,
                            99999
                    )
            );
            if (e.getPlayer().getItemInMainHand().material() != Material.AIR
            &&
                e.getPlayer().getItemInMainHand().hasTag(Tag.Integer("scoped"))) {

                int zoomLevel = e.getPlayer().getItemInMainHand().getTag(Tag.Integer("scoped"));
                if (zoomLevel == 0) {
                    zoomLevel = 2;
                } else if (zoomLevel == 2) {
                    zoomLevel = 4;
                } else if (zoomLevel == 4) {
                    zoomLevel = 0;
                }
                e.getPlayer().addEffect(
                        new Potion(PotionEffect.SLOWNESS,
                                (byte) zoomLevel,
                                9999
                        )
                );
                e.getPlayer().setItemInMainHand(
                        e.getPlayer().getItemInMainHand()
                                .withTag(Tag.Integer("scoped"), zoomLevel)
                );

            }
        });
    }

    private static Gun findGun(String gunName) {
        return guns.get(gunName);
    }

    @Nullable
    public static ItemStack getGunsItemstack(String tag) {
        return findGun(tag).getItemStack();
    }


}
