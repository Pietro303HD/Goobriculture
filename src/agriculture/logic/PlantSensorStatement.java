package agriculture.logic;

import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.ui.*;

public class PlantSensorStatement extends LStatement {
    public String result, obj;
    public PlantAccess sense = PlantAccess.waterLevel;

    public PlantSensorStatement(){
        this("result", "waterLevel", "block1");
    }

    public PlantSensorStatement(String result, String sense, String obj){
        this.result = result;
        this.obj = obj;

        try{
            this.sense = PlantAccess.valueOf(sense);
        }catch(IllegalArgumentException ignored){}
    }
    // thanks sk :)
    // i can't believe i did this
    @Override
    public void build(Table table) {
        table.clearChildren();

        table.table(t -> {
            t.left();
            t.setColor(table.color);
            field(t, result, res -> result = res);
            t.add(" = ");
        });
        row(table);

        table.table(t -> {
            t.left();
            t.setColor(table.color);

            TextField tfield = field(t, sense.name(), text -> {
                try{
                    sense = PlantAccess.valueOf(text);
                }catch(Exception ignored){}
            }).padRight(0f).get();

            Button b = new Button(Styles.logict);
            b.image(Icon.pencilSmall);
            b.clicked(() -> showSelect(b, PlantAccess.all, sense, t2 -> {
                tfield.setText(t2.name());
                sense = t2;
                build(table);
            }, 1, cell -> cell.size(240f, 40f)));
            t.add(b).color(table.color).size(40f).padLeft(-1f);
            t.add(" in ");

            field(t, obj, text -> obj = text);
        }).left();
    }

    @Override
    public void write(StringBuilder builder) {
        builder
            .append("plantsensor ")
            .append(result)
            .append(" ")
            .append(sense.name)
            .append(" ")
            .append(obj);
    }

    @Override
    public Color color() {
        return Color.valueOf("6bb27a");
    }

    @Override
    public LExecutor.LInstruction build(LAssembler builder) {
        return new PlantSenseI(builder.var(result), sense, builder.var(obj));
    }
}
