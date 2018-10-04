package aer.map.mapgen.mapgenpart;

import aer.locate.*;
import aer.map.maptiles.*;

public class FillerMapGenPart extends MapGenPart
{
	private final MapTileType mapTileType;

	public FillerMapGenPart(MapTileType mapTileType)
	{
		this.mapTileType = mapTileType;
	}

	@Override
	protected MapTile tile(HexLocation loc)
	{
		return new MapTile(map, loc, mapTileType);
	}
}