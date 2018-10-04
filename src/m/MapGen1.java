package m;

import aer.locate.*;
import aer.map.mapgen.*;
import aer.map.mapgen.mapgenpart.*;
import aer.map.maptiles.*;

public class MapGen1
{
	public static int[] getMapBounds()
	{
		return new int[]{-10, -2, -2, 0, 11, 10, 5, 1};
	}

	public static IHexMapGen generator()
	{
		IHexMapGen hwmgp = new HexWallMGP(new HexLocation(5, 5, 0, 0), 3);
		IHexMapGen sfhmg = SplitFillerHexMapGen.get(2, MapTileType.BLOCKED, MapTileType.BLOCKED,
				MapTileType.FLOOR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR);
		return new SplitHexMapGen(3, new StackedHexMapGen(hwmgp, sfhmg));
	}
}