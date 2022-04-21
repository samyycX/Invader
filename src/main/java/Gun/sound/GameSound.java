package Gun.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class GameSound {

    public static final Sound HIT_SOUND = Sound.sound(
            Key.key("entity.arrow.hit_player"),
            Sound.Source.AMBIENT,
            10,
            0
    );

}
