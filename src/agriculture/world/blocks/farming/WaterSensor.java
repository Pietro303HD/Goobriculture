package agriculture.world.blocks.farming;

import agriculture.world.blocks.*;
import arc.math.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;

public class WaterSensor extends AreaBlock {
    public WaterSensor(String name, float range){
        super(name, range);
        consumesPower = true;
        hasPower = true;
    }

    public class WaterSensorBuild extends AreaEffectBuild {
        public int count;
        public float average, total;

        @Override
        public void updateTile() {
            count = 0;
            average = total = 0f;
            super.updateTile();
        }

        @Override
        public boolean filter(Building b) {
            return b instanceof PlantBlock.PlantBuild;
        }

        @Override
        public void effect(Building b) {
            count++;
            total += ((PlantBlock.PlantBuild) b).waterLevel;
        }

        @Override
        public void whileValid() {
            average = total / count;
        }

        @Override
        public boolean valid() {
            return cons.valid() && enabled();
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            Vars.indexer.eachBlock(this, range, this::filter, b -> {
                if(valid()){
                    float lerp = ((100 - ((PlantBlock.PlantBuild) b).waterLevel) / 100f);
                    Tmp.c2.set(Liquids.water.color).lerp(Pal.accent, lerp).a(Mathf.absin(4f, 1f));
                }else{
                    Tmp.c2.set(radiusColor);
                }
                Drawf.selected(b, Tmp.c2);
            });
        }

        @Override
        public double sense(LAccess sensor) {
            if(sensor == LAccess.totalLiquids){
                return average;
            }
            return super.sense(sensor);
        }
    }
}
