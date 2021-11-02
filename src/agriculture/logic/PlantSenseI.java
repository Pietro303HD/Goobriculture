package agriculture.logic;

import agriculture.world.blocks.farming.*;
import mindustry.logic.LExecutor;

public class PlantSenseI implements LExecutor.LInstruction {
    public int result, obj;
    public PlantAccess sense;

    public PlantSenseI(int result, PlantAccess sense, int obj){
        this.sense = sense;
        this.result = result;
        this.obj = obj;
    }

    @Override
    public void run(LExecutor exec) {
        Object b = exec.obj(obj);

        double v = 0d;
        if(b instanceof PlantBlock.PlantBuild p){
            v = switch(sense){
                case health -> p.plant.health;
                case waterLevel -> p.waterLevel;
                case maturity -> p.plant.growth;
                case harvestYield -> p.plant.produce();
                case maxYield -> p.plant.type == null ? 0d : p.plant.type.itemAmount;
                case type -> p.plant.type == null ? -1d : p.plant.type.item.id;
                case minWater -> p.plant.type == null ? -1d : p.plant.type.minWater;
                case maxWater -> p.plant.type == null ? -1d : p.plant.type.maxWater;
                case ideal -> p.plant.waterValid() ? 1d : 0d;
                case alive -> p.plant.alive ? 1d : 0d;
            };
        }
        exec.setnum(result, v);
    }
}
