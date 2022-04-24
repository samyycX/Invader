package com.samyyc.invader.gun;

import com.samyyc.invader.Main;
import com.samyyc.invader.game.PlayerKillPlayerEvent;
import com.samyyc.invader.gun.GunImpl.*;
import com.samyyc.invader.gun.bullet.BulletManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.Instance;
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

import java.util.*;

public class GunsManager {

    private static final Map<String, Gun> guns = new HashMap<>();

    private static final Map<Instance, BulletManager> bulletManagerMap = new HashMap<>();

    public static void init() {
        addGun(new AssaultRifle());
        addGun(new AxototlGun());
        addGun(new TestGun());
        addGun(new RPG());
        addGun(new AWP());
    }

    public static void hookEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerUseItemEvent.class, e -> {

            String gunName = e.getItemStack().getTag(Tag.String("gun"));
            if (gunName != null) {
                Gun gun = findGun(gunName);
                if (!e.getItemStack().getTag(Tag.Boolean("reloading"))) {
                    if (gun.hasAmmo(e.getPlayer())) {
                        if ((System.currentTimeMillis() - e.getPlayer().getItemInMainHand().getTag(Tag.Long("lastRefresh"))) < (gun.getCoolDown() * MinecraftServer.TICK_PER_SECOND)) {
                            return;
                        }

                        if (!bulletManagerMap.containsKey(e.getInstance())) {
                            BulletManager bulletManager = new BulletManager(e.getInstance());
                            bulletManager.init();
                            bulletManagerMap.put(e.getInstance(), bulletManager);
                        }
                        gun.fire(e.getPlayer(), bulletManagerMap.get(e.getInstance()));
                        e.getPlayer().setItemInMainHand(e.getPlayer().getItemInMainHand()
                                .withTag(
                                    Tag.Integer("ammo"),
                                        e.getPlayer().getItemInMainHand().getTag(Tag.Integer("ammo")) - 1
                                )
                                .withTag(
                                        Tag.Long("lastRefresh"),
                                        System.currentTimeMillis()
                                )
                        );
                    } else {
                        gun.reloadAmmo(e.getPlayer(), e.getPlayer().getHeldSlot());
                    }
                }
            }
        });

        globalEventHandler.addListener(ItemDropEvent.class, e -> {
            if (e.getItemStack().hasTag(Tag.String("gun"))) {
                if (!e.getItemStack().getTag(Tag.Boolean("reloading"))) {
                    findGun(e.getItemStack().getTag(Tag.String("gun"))).reloadAmmo(e.getPlayer(), e.getPlayer().getHeldSlot());
                }
                e.setCancelled(true);
            }
        });

        globalEventHandler.addListener(PlayerHandAnimationEvent.class, e -> {
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

        globalEventHandler.addListener(PlayerChangeHeldSlotEvent.class, e->{
            e.setCancelled(true);
            if (e.getPlayer().getInventory().getItemStack(e.getSlot()).hasTag(Tag.Boolean("reloading"))) {
                boolean reloading = e.getPlayer().getInventory().getItemStack(e.getSlot()).getTag(Tag.Boolean("reloading"));
                if (!reloading) {
                    e.setCancelled(false);
                } else {
                    e.getPlayer().setHeldItemSlot(e.getSlot());
                }
            } else {
                e.setCancelled(false);
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

    public static void callKillingEvent(Player killer, Entity dead, double damage) {
        if (dead instanceof Player) {
            if (((Player) dead).getHealth() - damage <= 0) {
                killer.getInstance().eventNode().call(new PlayerKillPlayerEvent(killer, (Player) dead, killer.getInstance()));
            }
        }
    }

    public static void addGun(Gun gun) {
        guns.put(gun.getTag(), gun);
    }

}
