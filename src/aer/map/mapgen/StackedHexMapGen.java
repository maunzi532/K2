package aer.map.mapgen;

import aer.locate.*;
import aer.map.*;

public class StackedHexMapGen implements IHexMapGen
{
	private IHexMapGen[] generators;
	private ITiledMap map;

	public StackedHexMapGen(IHexMapGen... generators)
	{
		this.generators = generators;
	}

	@Override
	public void init(ITiledMap map)
	{
		this.map = map;
		for(int i = 0; i < generators.length; i++)
		{
			generators[i].init(map);
		}
	}

	@Override
	public void generate(HexLocation loc)
	{
		for(int i = 0; i < generators.length; i++)
		{
			generators[i].generate(loc);
			if(map.tileGenerated(loc))
				return;
		}
		throw new RuntimeException("Generated null: " + loc.toString());
	}
}