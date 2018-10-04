package aer.map.mapgen.mapgenpart;

import aer.locate.*;
import aer.map.*;
import aer.map.mapgen.*;
import aer.map.maptiles.*;

public abstract class MapGenPart implements IHexMapGen
{
	protected ITiledMap map;

	@Override
	public void init(ITiledMap map)
	{
		this.map = map;
	}

	@Override
	public void generate(HexLocation loc)
	{
		map.setTile(tile(loc));
	}

	protected abstract MapTile tile(HexLocation loc);
}