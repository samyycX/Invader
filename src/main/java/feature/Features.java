package feature;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public class Features{

    public static Feature combat() {
        return new CombatFeature();
    }
}
