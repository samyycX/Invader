package com.samyyc.invader.command.vanilla;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CTime extends Command {


    public CTime() {
        super("time", "time");

        ArgumentString time = new ArgumentString("time");

        addSyntax((sender, context) -> {
            String t = context.get(time);
            Player player = (Player) sender;
            if (t.equalsIgnoreCase("night")) {
                player.getInstance().setTime(18000);
            } else {
                player.getInstance().setTime(100);
            }
        }, time);
    }
}
