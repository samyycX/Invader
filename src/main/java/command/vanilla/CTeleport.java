package command.vanilla;

import kotlin.reflect.jvm.internal.impl.incremental.components.Position;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.position.PositionUtils;

public class CTeleport extends Command {

    public CTeleport() {
        super("tp","teleport");

        ArgumentDouble arg = ArgumentType.Double("X");
        ArgumentDouble arg2 = ArgumentType.Double("Y");
        ArgumentDouble arg3 = ArgumentType.Double("Z");

        addSyntax((sender, context) -> {
            final double x = context.get(arg);
            final double y = context.get(arg2);
            final double z = context.get(arg3);
            Player player = (Player) sender;
            Pos pos = new Pos(x, y, z);
            player.teleport(pos);

        }, arg, arg2, arg3);
    }
}
