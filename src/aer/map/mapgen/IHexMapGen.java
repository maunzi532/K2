package aer.map.mapgen;

import aer.locate.*;
import aer.map.*;

public interface IHexMapGen
{
	void init(ITiledMap map);

	void generate(HexLocation loc);
}