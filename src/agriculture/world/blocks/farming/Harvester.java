package agriculture.world.blocks.farming;

import agriculture.*;
import agriculture.world.blocks.*;
import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class Harvester extends AreaBlock {
    public TextureRegion headRegion, railRegion, edgeRegion, driverRegion;

    public float cooldown;

    public Harvester(String name, int range){
        super(name, range * 8);

        solid = true;
        update = true;

        hasItems = true;
        cooldown = 50f;
        itemCapacity = 60;

        loopSound = Sounds.spray;

        radiusColor = Pal.accent;

        envEnabled = Env.terrestrial;

        consumes.liquid(Liquids.water, 0.2f);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        AgriUtils.dashSquare(x * 8, y * 8, range * 2, range * 2, valid ? radiusColor : Color.scarlet);
    }

    @Override
    public void load() {
        super.load();
        headRegion = Core.atlas.find(name + "-head");
        railRegion = Core.atlas.find(name + "-rail");
        edgeRegion = Core.atlas.find(name + "-edge");
        driverRegion = Core.atlas.find(name + "-driver");
    }

    public class HarvesterBuild extends AreaEffectBuild {
        public float progress = 0f;
        public float harvestCooldown = 0f;
        public float headX = x, headY = y;
        public Seq<PlantBlock.PlantBuild> builds = new Seq<>();
        public PlantBlock.PlantBuild current;

        @Override
        public void updateTile() {
            for(int i = 0; i < builds.size; i++){
                PlantBlock.PlantBuild b = builds.get(i);
                if(!b.added) builds.remove(i);
            }
            if(valid()){
                team.data().buildings.intersect(x - range, y - range, range * 2, range * 2, b -> {
                    if(filter(b)) effect(b);
                });

                whileValid();

                progress += Time.delta;
            } else {
                progress -= Time.delta;
                headX = Mathf.lerpDelta(x, headX, progress / 100f);
                headY = Mathf.lerpDelta(y, headY, progress / 100f);
            }
            progress = Mathf.clamp(progress, 0f, 100f);
            dump();
        }

        @Override
        public void whileValid() {
            if(progress < 95f)return;
            if(current == null){
                if(builds.isEmpty()) return;
                current = builds.pop();
            }else if(harvestCooldown == 0){
                if(!Mathf.within(headX, headY, current.x, current.y, 1f)){
                    headX = Mathf.lerpDelta(headX, current.x, 0.1f);
                    headY = Mathf.lerpDelta(headY, current.y, 0.1f);
                }else{
                    if(current.plant.type == null){
                        current = null;
                        return;
                    }

                    Item item = current.plant.type.item;
                    if(items.get(item) + current.plant.produce() < itemCapacity){
                        int amount = current.removeStack(item, current.plant.produce());

                        float ex = current.x, ey = current.y;
                        for (int j = 0; j < Mathf.clamp(amount / 3, 1, 8); j++) {
                            Time.run(j * 3f, () -> {
                                Fx.itemTransfer.at(ex, ey, amount, item.color, this);
                            });
                        }
                        Time.run(Fx.itemTransfer.lifetime, () -> items.add(item, amount));

                        current = null;
                        harvestCooldown = cooldown;
                    }
                }
            }
            harvestCooldown -= Time.delta;
            harvestCooldown = Mathf.clamp(harvestCooldown, 0f, cooldown);
        }

        @Override
        public boolean filter(Building b) {
            return b instanceof PlantBlock.PlantBuild p && !builds.contains(p) && p != current && p.plant.type != null;
        }

        @Override
        public void effect(Building b) {
            builds.add(((PlantBlock.PlantBuild) b));
        }

        public int tileRange(){
            return (int) range / 8;
        }

        @Override
        public boolean valid() {
            return cons.valid() && enabled;
        }

        @Override
        public void draw() {
            float ox1, oy1, ox2, oy2;
            float lerp = Interp.smoother.apply(Mathf.curve(progress, 45, 100));
            float doorlerp = Mathf.curve(progress, 0, 15);

            Draw.color(Color.valueOf("9a9fb4"));
            Fill.rect(x, y, Math.max((size - 1) * 8f, 8f), Math.max((size - 1) * 8f, 8f));
            Draw.color(Pal.darkerGray);
            Fill.rect(x, y, Math.max((size - 1) * 8f, 8f) * doorlerp, Math.max((size - 1) * 8f, 8f));
            Draw.color();

            Draw.rect(region, x, y);

            Draw.alpha(Mathf.curve(progress, 15, 35));
            for(int i = 0; i < 4; i++){
                ox1 = Geometry.d8edge[i].x * (range * lerp);
                oy1 = Geometry.d8edge[i].y * (range * lerp);

                ox2 = Geometry.d8edge[(i + 1) % 4].x * (range * lerp);
                oy2 = Geometry.d8edge[(i + 1) % 4].y * (range * lerp);

                Draw.z(Layer.blockOver + 1);
                Lines.stroke(4f);
                Lines.line(railRegion, x + ox1, y + oy1, x + ox2, y + oy2, true);

                Draw.z(Layer.blockOver + 2);
                Draw.rect(edgeRegion, x + ox1, y + oy1);
            }
            Draw.rect(headRegion, headX, headY);
        }

        @Override
        public void drawSelect() {
            AgriUtils.dashSquare(x, y, range * 2, range * 2, radiusColor);
            for(Building b : builds){
                Drawf.selected(b, Pal.accent);
            }
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();
            drawSelect();
        }
    }
}
