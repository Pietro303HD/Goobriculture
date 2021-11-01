package agriculture.world.blocks.farming;

import agriculture.*;
import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import mindustry.*;

public class Farmland extends PlantBlock{
    public TextureRegion[] baseRegions, soilRegions, wetSoilRegions;
    public Farmland(String name){
        super(name);

        solid = false;
        hasShadow = false;
    }

    @Override
    public void load() {
        super.load();
        baseRegions = AgriUtils.getRegions(Core.atlas.find(name + "-tiled"), 12, 4);
        soilRegions = AgriUtils.getRegions(Core.atlas.find(name + "-soil-tiled"), 12, 4);
        wetSoilRegions = AgriUtils.getRegions(Core.atlas.find(name + "-soil-wet-tiled"), 12, 4);
    }

    public class FarmlandBuild extends PlantBuild {
        public int mask;
        @Override
        public void draw() {
            mask = 0;
            for(int i = 0; i < 8; i++){
                if(Vars.world.build(tileX() + Geometry.d8[i].x, tileY() + Geometry.d8[i].y) instanceof FarmlandBuild){
                    mask |= 1 << i;
                }
            }
            drawSoil();
            Draw.rect(baseRegions[AgriUtils.tiles[mask]], x, y);
            plant.draw();
        }

        @Override
        public void drawSoil() {
            Draw.rect(soilRegions[AgriUtils.tiles[mask]], x, y);
            Draw.alpha(waterLevel / 100f);
            Draw.rect(wetSoilRegions[AgriUtils.tiles[mask]], x, y);
            Draw.alpha(1);
        }
    }
}
