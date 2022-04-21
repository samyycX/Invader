package mob;

import java.util.List;

@FunctionalInterface
public interface MobGenerator {

    List<CustomMob> generate();

}
