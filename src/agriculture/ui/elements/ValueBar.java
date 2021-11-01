package agriculture.ui.elements;

import arc.func.*;
import arc.graphics.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.ui.*;

public class ValueBar extends Table {
    public ValueBar(String name, Prov<String> text, Color color, Floatp val, float maxVal){
        this.name = name;
        table(t -> {
            t.labelWrap(text::get)
                .top().left()
                .width(80f)
                .get().setAlignment(Align.center);
            t.add(new Bar(name, color, () -> val.get() / maxVal)).align(Align.left).growX();
        }).growX();
    }
}
