package map;

import java.util.Arrays;

public class Map
{
    private static char[][] map;

    public static void setMap(final char[][] map)
    {
        Map.map = new char[map.length][map[0].length];
        for(int i = 0; i < map.length; ++i)
            Map.map[i] = Arrays.copyOf(map[i], map[i].length);
    }

    public static char getTerrain(final Position position)
    {
        return map[position.x][position.y];
    }
}
