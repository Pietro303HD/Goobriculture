package agriculture;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.*;
import mindustry.*;
import mindustry.graphics.Drawf;
import mindustry.ui.*;

public class AgriUtils {
    public static int[] tiles = new int[]{
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        3,  4,  3,  4, 15, 40, 15, 20,  3,  4,  3,  4, 15, 40, 15, 20,
        5, 28,  5, 28, 29, 10, 29, 23,  5, 28,  5, 28, 31, 11, 31, 32,
        3,  4,  3,  4, 15, 40, 15, 20,  3,  4,  3,  4, 15, 40, 15, 20,
        2, 30,  2, 30,  9, 46,  9, 22,  2, 30,  2, 30, 14, 44, 14,  6,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        39, 36, 39, 36, 27, 16, 27, 24, 39, 36, 39, 36, 27, 16, 27, 24,
        38, 37, 38, 37, 17, 41, 17, 43, 38, 37, 38, 37, 26, 21, 26, 25,
        3,  0,  3,  0, 15, 42, 15, 12,  3,  0,  3,  0, 15, 42, 15, 12,
        5,  8,  5,  8, 29, 35, 29, 33,  5,  8,  5,  8, 31, 34, 31,  7,
        3,  0,  3,  0, 15, 42, 15, 12,  3,  0,  3,  0, 15, 42, 15, 12,
        2,  1,  2,  1,  9, 45,  9, 19,  2,  1,  2,  1, 14, 18, 14, 13
    };

    /**
     * @author Xelo
     * Gets multiple regions inside a {@link TextureRegion}. The size for each region has to be 32.
     * @param w The amount of regions horizontally.
     * @param h The amount of regions vertically.
     */
    public static TextureRegion[] getRegions(TextureRegion region, int w, int h){
        int size = w * h;
        TextureRegion[] regions = new TextureRegion[size];

        float tileW = (region.u2 - region.u) / w;
        float tileH = (region.v2 - region.v) / h;

        for(int i = 0; i < size; i++){
            float tileX = ((float)(i % w)) / w;
            float tileY = ((float)(i / w)) / h;
            TextureRegion reg = new TextureRegion(region);

            //start coordinate
            reg.u = Mathf.map(tileX, 0f, 1f, reg.u, reg.u2) + tileW * 0.02f;
            reg.v = Mathf.map(tileY, 0f, 1f, reg.v, reg.v2) + tileH * 0.02f;
            //end coordinate
            reg.u2 = reg.u + tileW * 0.96f;
            reg.v2 = reg.v + tileH * 0.96f;

            reg.width = reg.height = 32;

            regions[i] = reg;
        }
        return regions;
    }

    public static void dashSquare(float x, float y, float w, float h, Color color){
        Drawf.dashLine(color, x + w/2, y - h/2, x + w/2, y + h/2);
        Drawf.dashLine(color, x - w/2, y - h/2, x - w/2, y + h/2);

        Drawf.dashLine(color, x - w/2, y + h/2, x + w/2, y + h/2);
        Drawf.dashLine(color, x - w/2, y - h/2, x + w/2, y - h/2);
    }

    public static void lightSquare(float x, float y, float radius, Color center, Color edge){
        float centerf = center.toFloatBits(), edgef = edge.toFloatBits();

        float px = Geometry.d8edge[0].x * radius + x;
        float py = Geometry.d8edge[0].y * radius + y;
        float px1 = Geometry.d8edge[1].x * radius + x;
        float py1 = Geometry.d8edge[1].y * radius + y;
        float px2 = Geometry.d8edge[2].x * radius + x;
        float py2 = Geometry.d8edge[2].y * radius + y;
        float px3 = Geometry.d8edge[3].x * radius + x;
        float py3 = Geometry.d8edge[3].y * radius + y;

        Fill.quad(x, y, centerf, px, py, edgef, px1, py1, edgef, px2, py2, edgef);
        Fill.quad(x, y, centerf, px2, py2, edgef, px3, py3, edgef, px, py, edgef);
    }

    public static void drawText(String text, float x, float y, Color color){
        if(Vars.renderer.pixelator.enabled()) return;

        Font font = Fonts.def;
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(1f);

        font.setColor(color);
        font.draw(text, x, y, Align.left);

        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1f);
        Draw.reset();
    }
}
