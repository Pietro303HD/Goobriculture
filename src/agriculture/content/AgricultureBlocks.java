package agriculture.content;

import agriculture.world.blocks.farming.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.world.*;

public class AgricultureBlocks implements ContentList {
    public static Block farmland, sprinklerSmall, harvester, sensorSmall;

    @Override
    public void load() {
        farmland = new Farmland("farmland");
        sprinklerSmall = new Waterer("small-sprinkler", 60f, 1.5f){{
            consumes.power(0.2f);
            consumes.liquid(Liquids.water, 0.2f);
        }};
        harvester = new Harvester("harvester", 10){{
            consumes.power(0.2f);
            itemCapacity = 60;
            size = 3;
        }};
        sensorSmall = new WaterSensor("small-water-sensor", 60f){{
            consumes.power(0.5f);
        }};
    }
}
