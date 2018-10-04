package m;

import aer.map.mapgen.*;
import aer.map.maptiles.*;

public class MapGen1
{
	public static int[] getMapBounds()
	{
		return new int[]{-10, -2, -2, 0, 11, 10, 5, 1};
	}

	public static IHexMapGen generator()
	{
		return new SplitHexMapGen(3, SplitFillerHexMapGen.get(2, MapTileType.BLOCKED, MapTileType.BLOCKED,
				MapTileType.FLOOR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR));
	}
}