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
		IHexMapGen hwmgp0 = new HexWallMGP(new HexLocation(5, 5, 0, 0), 3, HWMGPType.GROUND);
		IHexMapGen hwmgp1 = new HexWallMGP(new HexLocation(5, 5, 1, 0), 3, HWMGPType.FLOOR);
		IHexMapGen hwmgp2 = new HexWallMGP(new HexLocation(5, 5, 2, 0), 3, HWMGPType.FLOOR);
		IHexMapGen hwmgp3 = new HexWallMGP(new HexLocation(5, 5, 3, 0), 3, HWMGPType.CEILING);
		IHexMapGen sfhmg = SplitFillerHexMapGen.get(2, MapTileType.BLOCKED, MapTileType.BLOCKED,
				MapTileType.FLOOR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR, MapTileType.AIR);
		return new SplitHexMapGen(3, new StackedHexMapGen(hwmgp0, hwmgp1, hwmgp2, hwmgp3, sfhmg));
	}
}