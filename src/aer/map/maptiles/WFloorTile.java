package aer.map.maptiles;

import aer.locate.*;
import aer.map.*;

public class WFloorTile extends MapTile
{
	public final int direction;
	public final WallType wallType;

	public WFloorTile(ITiledMap map, HexLocation loc, int direction, WallType wallType)
	{
		super(map, loc, MapTileType.FLOOR);
		this.direction = direction;
		this.wallType = wallType;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof WFloorTile)) return false;
		if(!super.equals(o)) return false;

		WFloorTile that = (WFloorTile) o;

		if(direction != that.direction) return false;
		return wallType == that.wallType;
	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + direction;
		result = 31 * result + wallType.hashCode();
		return result;
	}
}