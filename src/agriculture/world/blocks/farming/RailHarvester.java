package agriculture.world.blocks.farming;

import agriculture.world.blocks.ConnectedBlock;
import arc.Core;
import arc.graphics.g2d.*;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Item;

public class RailHarvester extends ConnectedBlock {
    public TextureRegion railRegion, headRegion;
    public RailHarvester(String name, int range){
        super(name, range);

        hasItems = true;
        hasPower = true;
        itemCapacity = 40;
    }

    @Override
    public boolean linkValid(Building b, Building other) {
        return other instanceof RailHarvesterBuild && positionsValid(b.tileX(), b.tileY(), other.tileX(), other.tileY());
    }

    public boolean positionsValid(int x1, int y1, int x2, int y2){
        if(x1 == x2){
            return Math.abs(y1 - y2) <= range;
        }else if(y1 == y2){
            return Math.abs(x1 - x2) <= range;
        }else{
            return false;
        }
    }

    @Override
    public void drawConnection(ConnectedBuild build, ConnectedBuild other) {
        float progress = Interp.pow3Out.apply(((RailHarvesterBuild) build).railProgress / 100f);
        float xo = build.x + (other.x - build.x) * progress;
        float yo = build.y + (other.y - build.y) * progress;
        Lines.stroke(8f);
        Lines.line(railRegion, build.x, build.y, xo, yo, false);
    }

    @Override
    public void load() {
        super.load();
        headRegion = Core.atlas.find(name + "-head");
        railRegion = Core.atlas.find(name + "-rail");
    }

    public class RailHarvesterBuild extends ConnectedBuild {
        public float headX, headY;
        public boolean arrived;
        public float progress, railProgress;

        @Override
        public void created() {
            super.created();
            headX = x;
            headY = y;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if(valid()){
                if(progress > 95f) updateHead();
                progress = Mathf.clamp(railProgress + Time.delta, 0f, 100f);
            }else{
                headX = Mathf.approach(headX, x, Time.delta);
                headY = Mathf.approach(headY, y, Time.delta);
                progress = 0;
            }
            progress = Mathf.clamp(progress, 0f, 100f);

            if(link != null){
                railProgress = Mathf.clamp(railProgress + Time.delta, 0f, 100f);
            }else{
                railProgress = Mathf.clamp(railProgress - Time.delta, 0f, 100f);
            }
            dump();
        }

        @Override
        public void linkChanged() {
            railProgress = 0;
            progress = 0;
            headX = x;
            headY = y;
        }

        public void updateHead() {
            if(link != null){
                if(!arrived) {
                    if (headX != link.x || headY != link.y) {
                        headX = Mathf.approach(headX, link.x, Time.delta);
                        headY = Mathf.approach(headY, link.y, Time.delta);
                    } else {
                        arrived = true;
                    }
                }else{
                    if (headX != x || headY != y) {
                        headX = Mathf.approach(headX, x, Time.delta);
                        headY = Mathf.approach(headY, y, Time.delta);
                    } else {
                        arrived = false;
                    }
                }

                Building b = Vars.world.buildWorld(headX, headY);
                if(b instanceof PlantBlock.PlantBuild p && Mathf.within(headX, headY, b.x, b.y, 4f)){
                    Log.info("found plant");
                    if(p.isAdded() && p.plant.type != null){
                        Item item = p.plant.type.item;
                        Log.info("harvest?");
                        Log.info(items.get(item) + p.plant.produce());
                        Log.info(block.itemCapacity);
                        Log.info(items.get(item) + p.plant.produce() < block.itemCapacity);
                        if(items.get(item) + p.plant.produce() < block.itemCapacity){
                            Log.info("harvesting");
                            int amount = p.removeStack(item, p.plant.produce());

                            float ex = p.x, ey = p.y;
                            for (int j = 0; j < Mathf.clamp(amount / 3, 1, 8); j++) {
                                Time.run(j * 3f, () -> {
                                    Fx.itemTransfer.at(ex, ey, amount, item.color, this);
                                });
                            }

                            Time.run(Fx.itemTransfer.lifetime, () -> items.add(item, amount));
                        }
                    }
                }
            }
        }

        @Override
        public void draw() {
            Draw.z(Layer.blockOver);
            if(link != null) drawConnection(this, link);
            Draw.z(Layer.blockOver + 1);
            Draw.rect(region, x, y);
            Draw.z(Layer.blockOver + 2);
            Draw.rect(headRegion, headX, headY);
        }

        public boolean valid(){
            return cons.valid() && enabled && link != null;
        }
    }
}
