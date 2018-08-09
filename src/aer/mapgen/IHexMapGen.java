package aer.mapgen;

import aer.*;

public interface IHexMapGen
{
	void init(ITiledMap map);

	void generate(HexLocation loc);
}