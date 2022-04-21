package feature;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.utils.time.TimeUnit;

import java.time.Duration;
import java.util.Random;

public record CombatFeature() implements Feature {

    @Override
    public void hook(EventNode<Event> node) {
        node.addListener(EntityAttackEvent.class, e -> {
            if (e.getTarget() instanceof LivingEntity target) {

                int damage = 1;
                target.damage(DamageType.fromEntity(e.getEntity()), damage);

                Hologram hologram = new Hologram(
                        target.getInstance(),
                        target.getPosition().add(0, target.getEyeHeight(), 0),
                        Component.text(damage, NamedTextColor.RED, TextDecoration.BOLD)
                );


                Random random = new Random();
                float rotX = random.nextFloat(180);
                float rotY = random.nextFloat(180);
                double xz = Math.cos(Math.toRadians((double)rotY));
                Vec vec =  new Vec(-xz * Math.sin(Math.toRadians((double)rotX)), -Math.sin(Math.toRadians((double)rotY)), xz * Math.cos(Math.toRadians((double)rotX)));

                hologram.getEntity().setVelocity(
                        vec.withY(-1).normalize().mul(3)
                );
                MinecraftServer.getSchedulerManager()
                        .buildTask(hologram::remove)
                        .delay(Duration.of(30, TimeUnit.SERVER_TICK))
                        .schedule();

            }
        });
    }
}
