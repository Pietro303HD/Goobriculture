package agriculture.logic;

public enum PlantAccess {
    health("health"),
    alive("alive"),
    type("type"),
    maturity("maturity"),
    harvestYield("harvestYield"),
    maxYield("maxYield"),
    waterLevel("waterLevel"),
    minWater("minWater"),
    maxWater("maxWater"),
    ideal("ideal");

    public static final PlantAccess[] all = values();

    public final String name;

    PlantAccess(String name){
        this.name = name;
    }
}
