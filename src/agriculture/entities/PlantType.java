package agriculture.entities;

import agriculture.content.*;
import arc.*;
import arc.util.*;
import mindustry.type.*;

public class PlantType {
    public int id;
    public int itemAmount;
    public Item item;

    public float minWater = 25f;
    public float maxWater = 75f;
    public float growthSpeed = 0.5f;
    public float absorbSpeed = 1f;

    public float minGrowth = 0f;

    public String name;

    public PlantType(Item item, int itemAmount){
        this.name = Core.bundle.get("plant." + item.toString() + ".name");
        this.item = item;
        this.itemAmount = itemAmount;

        Plants.register(this);
    }

    public void update(Plant plant){
        plant.growth += (growthSpeed / 60f) * Time.delta;
    }

    public void die(Plant plant){
    }

    public void draw(Plant plant){
    }
}
