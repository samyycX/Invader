package com.samyyc.invader.command.vanilla;

import com.samyyc.invader.name.T;
import com.samyyc.invader.util.Pair;
import com.samyyc.invader.util.TextUtil;
import com.samyyc.invader.util.logger.Logger;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.PermissionHandler;
import net.minestom.server.thread.Acquirable;

import javax.xml.crypto.Data;

public class CGamemode extends Command {

    public CGamemode() {
        super("gamemode", T.COMMAND_GAMEMODE_ALIAS.toString());

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(T.COMMAND_ERROR_NO_EXECUTOR.toString());
        });

        ArgumentString stringArg = ArgumentType.String("mode");

        addSyntax((sender, context) -> {

            if (!check(sender)) return;

           final String arg = context.get(stringArg);
           final Player player = (Player) sender;

           try {
               GameMode mode = GameMode.valueOf(arg.toUpperCase());
               player.setGameMode(mode);

               String hint = TextUtil.format(T.COMMAND_GAMEMODE_HINT, Pair.of("mode", mode.name()));
               player.sendMessage(hint);
               Logger.log(T.LOGGER_COMMAND_GAMEMODE_CHANGE, Pair.of("player", player.getName()), Pair.of("mode", mode.name()));
           } catch (IllegalArgumentException e) {
               try {
                   int modeId = Integer.parseInt(arg);
                   GameMode mode = GameMode.fromId(modeId);
                   player.setGameMode(mode);
                   String hint = TextUtil.format(T.COMMAND_GAMEMODE_HINT, Pair.of("mode", mode.name()));
                   player.sendMessage(hint);
                   Logger.log(T.LOGGER_COMMAND_GAMEMODE_CHANGE, Pair.of("player", player.getName()), Pair.of("mode", mode.name()));
               } catch (NumberFormatException e2) {
                   String hint = TextUtil.format(T.COMMAND_GAMEMODE_ERROR, Pair.of("mode", arg));
                   player.sendMessage(hint);
               }
           }
        }, stringArg);
    }

    public boolean check(CommandSender sender) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(T.COMMAND_ERROR_NOT_SUPPORT_CONSOLE.toString());
            return false;
        }

        Acquirable<PermissionHandler> acquirable = Acquirable.of(sender);

        return true;
    }


}
