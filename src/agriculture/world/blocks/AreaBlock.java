package agriculture.world.blocks;

import arc.graphics.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.BuildVisibility;

public class AreaBlock extends Block {
    public float range = 40f;
    public Color radiusColor = Pal.logicOperations;
    public AreaBlock(String name, float range){
        super(name);
        this.range = range;

        solid = true;
        update = true;
        updateInUnits = false;

        buildVisibility = BuildVisibility.shown;
        clipSize = range;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        Drawf.dashCircle(x * 8, y * 8, range, radiusColor);
    }

    public class AreaEffectBuild extends Building {
        @Override
        public void updateTile() {
            super.updateTile();
            if(valid()){
                Vars.indexer.eachBlock(this, range, this::filter, this::effect);
                whileValid();
            }
        }

        /** whether the building should execute its area effect or not */
        public boolean valid(){
            return enabled;
        }

        /** executed while {@link AreaEffectBuild#valid()} is true */
        public void whileValid(){
        }

        /** whether or not this building should be affected */
        public boolean filter(Building b){
            return false;
        }

        /** what happens to buildings that are affected */
        public void effect(Building b){
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Drawf.dashCircle(x, y, range, radiusColor);
        }
    }
}
