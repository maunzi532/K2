package aer.map.maptiles;

import aer.locate.*;
import aer.locate.pathseeker.*;
import aer.map.*;

public class WallTile extends MapTile
{
	public final int direction;
	public final WallType wallType;

	public WallTile(ITiledMap map, HexLocation loc, int direction, WallType wallType)
	{
		super(map, loc, MapTileType.BLOCKED);
		this.direction = direction;
		this.wallType = wallType;
	}

	@Override
	public int exitingCost(PDirection to, MovementTileType mType)
	{
		return 0;
	}

	@Override
	public int enteringCost(PDirection from, MovementTileType mType)
	{
		return -1;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof WallTile)) return false;
		if(!super.equals(o)) return false;

		WallTile wallTile = (WallTile) o;

		if(direction != wallTile.direction) return false;
		return wallType == wallTile.wallType;
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