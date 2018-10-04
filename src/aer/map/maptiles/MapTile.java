package aer.map.maptiles;

import aer.locate.*;
import aer.locate.pathseeker.*;
import aer.map.*;

public class MapTile
{
	public final ITiledMap map;
	public final HexLocation loc;
	public final MapTileType type;

	public MapTile(ITiledMap map)
	{
		this.map = map;
		loc = null;
		type = MapTileType.BLOCKED;
	}

	public MapTile(ITiledMap map, HexLocation loc, MapTileType type)
	{
		this.map = map;
		this.loc = loc;
		this.type = type;
	}

	public int exitingCost(PDirection to, MovementTileType mType)
	{
		if(type == MapTileType.FLOOR && to.h == -1)
			return -1;
		return 0;
	}

	public int enteringCost(PDirection from, MovementTileType mType)
	{
		if(type == MapTileType.BLOCKED)
			return -1;
		if(type == MapTileType.FLOOR && from.h == 1)
			return -1;
		return 1;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof MapTile)) return false;

		MapTile mapTile = (MapTile) o;

		if(!map.equals(mapTile.map)) return false;
		if(loc != null ? !loc.equals(mapTile.loc) : mapTile.loc != null) return false;
		return type == mapTile.type;
	}

	@Override
	public int hashCode()
	{
		int result = map.hashCode();
		result = 31 * result + (loc != null ? loc.hashCode() : 0);
		result = 31 * result + type.hashCode();
		return result;
	}
}