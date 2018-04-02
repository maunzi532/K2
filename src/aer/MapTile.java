package aer;

public class MapTile
{
	public final IHexMap map;
	public final HexLocation loc;
	public MapTileType type;
	public final boolean outofbounds;

	public MapTile(IHexMap map, HexLocation loc)
	{
		this.map = map;
		this.loc = loc;
		type = MapTileType.BLOCKED;
		outofbounds = true;
	}

	public MapTile(IHexMap map, HexLocation loc, MapTileType type)
	{
		this.map = map;
		this.loc = loc;
		this.type = type;
		outofbounds = false;
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