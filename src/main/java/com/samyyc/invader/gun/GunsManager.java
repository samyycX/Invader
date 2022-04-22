package com.samyyc.invader.gun;

import com.samyyc.invader.Main;
import com.samyyc.invader.gun.GunImpl.AssaultRifle;
import com.samyyc.invader.gun.GunImpl.RPG;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class GunsManager {

    private static final Map<String, Gun> guns = new HashMap<>();

    public static void init() {
        try {
            Enumeration<URL> gunClasses = GunsManager.class.getClassLoader().getResources("com/samyyc/invader/gun/GunImpl");
            System.out.println(gunClasses);
            while (gunClasses.hasMoreElements()) {
                URL url = gunClasses.nextElement();
                String[] file = new File(url.getFile()).list();
                Class[] classList = new Class[file.length];
                for (int i = 0; i < classList.length; i++) {
                    Class<?> gunClass = Class.forName("com.samyyc.invader.gun.GunImpl."+file[i].replaceAll("\\.class",""));
                    if (Gun.class.isAssignableFrom(gunClass)) {
                        Constructor<?> constructor = gunClass.getConstructor();
                        guns.put(file[i].toLowerCase().replace(".class",""), (Gun) constructor.newInstance());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void hookEvent(EventNode<Event> node) {
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
    }

    private static Gun findGun(String gunName) {
        return guns.get(gunName);
    }

    @Nullable
    public static ItemStack getGunsItemstack(String tag) {
        return findGun(tag).getItemStack();
    }

}
