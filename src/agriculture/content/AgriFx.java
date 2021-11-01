package agriculture.content;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;

public class AgriFx {
    public static Effect

    splash = new Effect(10f, e -> {
        Draw.color(e.color);
        Draw.alpha(e.fout());
        for(int i = -1; i < 2; i+=2){
            Tmp.v2.trns(90 + (45 * i), e.fin(Interp.pow3Out) * 4);
            Lines.lineAngle(e.x + Tmp.v2.x, e.y + Tmp.v2.y, 90 + (45 * i), 2 * e.fout(Interp.pow3In));
        };
    }).layer(Layer.blockOver);
}
