package com.samyyc.invader.command.vanilla;

import com.samyyc.invader.Main;
import com.samyyc.invader.game.singlemode.SingleGameManager;
import com.samyyc.invader.gun.GunsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.map.MapColors;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public class CTest extends Command {

    private final InstanceContainer instance;
    public CTest(InstanceContainer instance) {
        super("test","test");
        this.instance = instance;

        addSyntax((sender, context) -> {

            Player player = (Player) sender;

            //player.getInventory().addItemStack(GunsManager.getGunsItemstack("rpg"));

            ItemStack map = ItemStack.builder(Material.FILLED_MAP)
                    .displayName(Component.text("test", NamedTextColor.GREEN))
                    .build()
                    .withTag(Tag.Integer("map"), 10);
            DirectFramebuffer directFramebuffer = new DirectFramebuffer();
            directFramebuffer.set(0, 0, MapColors.ICE.baseColor());
            player.sendPackets(directFramebuffer.preparePacket(10));
            player.getInventory().addItemStack(map);

            player.getInventory().addItemStack(GunsManager.getGunsItemstack("rpg"));
            player.getInventory().addItemStack(GunsManager.getGunsItemstack("assaultrifle"));
            player.getInventory().addItemStack(GunsManager.getGunsItemstack("awp"));
            //player.getInventory().addItemStack(GunsManager.getGunsItemstack("testrpg"));
            player.getInventory().addItemStack(GunsManager.getGunsItemstack("testgun"));
            player.getInventory().addItemStack(GunsManager.getGunsItemstack("axototlgun"));


            //Main.meteoriteGameManager.join(player);

        });
    }}
