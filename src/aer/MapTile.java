package aer;

public class MapTile
{
	public final ITiledMap map;
	public final HexLocation loc;
	public final MapTileType type;
	public final boolean outofbounds;

	public MapTile(ITiledMap map, HexLocation loc)
	{
		this.map = map;
		this.loc = loc;
		type = MapTileType.BLOCKED;
		outofbounds = true;
	}

	public MapTile(ITiledMap map, HexLocation loc, MapTileType type)
	{
		this.map = map;
		this.loc = loc;
		this.type = type;
		outofbounds = false;
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
		return outofbounds == mapTile.outofbounds && map == mapTile.map
				&& loc.equals(mapTile.loc) && type == mapTile.type;
	}

	@Override
	public int hashCode()
	{
		int result = map.hashCode();
		result = 31 * result + loc.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + (outofbounds ? 1 : 0);
		return result;
	}
}