package world;

public final class WorldManager {

    private final WorldMap worldMap;

    public WorldManager(int width, int height, String mapResource) {
        worldMap = new WorldMap(width, height);
        worldMap.load(mapResource);
    }

    public WorldMap getMap() {
        return worldMap;
    }
}
