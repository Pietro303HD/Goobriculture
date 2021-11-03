package agriculture.world.blocks;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class ConnectedBlock extends Block {
    public int range;
    public ConnectedBlock(String name, int range){
        super(name);
        this.range = range;

        solid = true;
        update = true;
        updateInUnits = false;

        buildVisibility = BuildVisibility.shown;
        configurable = true;

        config(Point2.class, (ConnectedBuild b, Point2 p) -> {
            b.linkPos = Point2.pack(p.x, p.y);
        });
    }

    public boolean linkValid(Building b, Building other){
        return b instanceof ConnectedBuild;
    }

    public void drawConnection(ConnectedBuild build, ConnectedBuild other){
        Lines.line(build.x, build.y, other.x, other.y);
    }

    public class ConnectedBuild extends Building {
        private ConnectedBuild linkPrev;
        public ConnectedBuild link;
        public int linkPos;

        @Override
        public void updateTile() {
            super.updateTile();
            updateLink();
            if(linkPrev != link) linkChanged();
            linkPrev = link;
        }

        public void updateLink() {
            Point2 unpacked = Point2.unpack(linkPos);
            Building b = Vars.world.build(unpacked.x + tileX(), unpacked.y + tileY());
            if(linkValid(this, b)){
                link = (ConnectedBuild) b;
            }else{
                link = null;
            }
        }

        public void setLink(int x, int y){
            linkPos = Point2.pack(x, y);
            linkChanged();
            updateLink();
        }

        public void setLink(Building b){
            int x = b.tileX() - tileX();
            int y = b.tileY() - tileY();
            setLink(x, y);
            updateLink();
        }

        @Override
        public boolean onConfigureTileTapped(Building other) {
            if(linkValid(this, other)){
                if(link == other){
                    disconnect();
                }else if(other != self() && ((ConnectedBuild) other).link != this){
                    disconnect();
                    setLink(other);
                }
                return false;
            }
            return true;
        }

        @Override
        public void draw() {
            super.draw();
            if(link != null) drawConnection(this, link);
        }

        public void disconnect(){
            linkPos = 0;
            link = null;
        }

        public void linkChanged(){

        }

        @Override
        public Object config() {
            return Point2.unpack(linkPos);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            if(revision >= 2){
                linkPos = read.i();
            }
        }

        @Override
        public byte version() {
            return 2;
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.i(linkPos);
        }
    }
}
