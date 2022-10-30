package map;

import java.util.Arrays;

public class Map
{
    private static Map instance = null;
    private static char[][] map;

    private Map() {}
    public static Map getInstance()
    {
        if(instance == null)
            instance = new Map();
        return instance;
    }

    public void setMap(final char[][] map)
    {
        Map.map = new char[map.length][map[0].length];
        for(int i = 0; i < map.length; ++i)
            Map.map[i] = Arrays.copyOf(map[i], map[i].length);
    }

    public char getTerrain(final Position position)
    {
        return map[position.x][position.y];
    }
}
